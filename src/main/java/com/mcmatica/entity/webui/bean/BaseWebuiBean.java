package com.mcmatica.entity.webui.bean;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;

import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.FieldModel;
import com.mcmatica.entity.webui.service.BaseWebuiService;


public  abstract class  BaseWebuiBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7423178114110032725L;

//	private static final String XHTML_ROWID = "rowId";
	
	protected BaseWebuiService service;
	
	protected List<FieldModel> columns = new ArrayList<FieldModel>();
	
	protected List<BaseEntityModel> list;
	
	protected List<BaseEntityModel> listFiltered;
	
	protected Panel formPanel;
	
	protected DataTable selectionGrid;
	
//	protected boolean editing;
	
	public abstract void init();
	
	
/*
 * ---------------------------------
 * B I N D I N G S
 */
	


	public List<BaseEntityModel> getList() {
		if (this.list == null)
		{
			this.list = this.service.buildList();
			//this.listFiltered = this.service.buildList();
			this.listFiltered = this.list;
		}
		return list;
	}



	public void setList(List<BaseEntityModel> list) {
		this.list = list;
	}
	
	public List<BaseEntityModel> getListFiltered() {
		if (this.listFiltered == null) {
			this.listFiltered = this.service.buildList();
		}
		return listFiltered;
	}


	public void setListFiltered(List<BaseEntityModel> listFiltered) {
		this.listFiltered = listFiltered;
	}
	
	
	public Panel getPanel()
	{
		if (formPanel == null)
		{
			this.formPanel = this.service.buildPanelGrid();
		}
		return formPanel;
	}

	public void setPanel(Panel panel)
	{
		this.formPanel = panel;
	}	
	
	public DataTable getSelectionGrid() {
		if (selectionGrid == null)
		{
			selectionGrid = this.service.buildSelectionGrid();
		}
		return selectionGrid;
	}


	public void setSelectionGrid(DataTable selectionGrid) {
		this.selectionGrid = selectionGrid;
	}


	public BaseEntityModel getSelected() {
		return service.getSelected();
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(BaseEntityModel selected) {
		this.service.setSelected(selected);
	}	
	
	/**
	 * Used for selection editor components
	 * @return
	 */
	public List<BaseEntityModel> findAll()
	{
		return this.service.findAll();
	}

	public boolean isEditing() {
		if (this.getSelected() != null)
		{
			return this.service.isEditing();
		}
		return true;
		
	}

//	public void setEditing(boolean value)
//	{
//		this.editing = value;
//	}

	
	
/*
 * ---------------------------------
 * C O M M A N D
 */
	
	
	public void save()
	{
		this.service.save();
//		this.listFiltered = null;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "save"));
	}
	
	public void create()
	{
		this.service.create();
		this.service.setDefaultValues();
//		this.editing = true;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "addnew"));

	}
	
	public void delete()
	{
		this.service.delete(this.service.getSelected());
//		this.editing = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "delete"));
		
	}
	
	public void refresh()
	{
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "refresh"));
		
	}
	
	public void removeFieldListItem(String childListName, BaseEntityModel item) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		/*
		 * Put the removed item into the special list used for cascading save
		 */
		this.getSelected().getFieldListItemsRemoved(childListName).add(item);
		
		Method mth = this.getSelected().getClass().getMethod("get" + Utility.capitalize(childListName) );
		List<BaseEntityModel> list =  (List<BaseEntityModel>) mth.invoke(this.getSelected());
		list.remove(item);
		
	}
	
	public void onChangeField(AjaxBehaviorEvent event)
	{
		RequestContext.getCurrentInstance().update("form_toolbar");
	}
	
	
	public abstract String goToHome();


	
	
/*
 * ---------------------------------------
 * P R I V A T E	
 */
	
	
}
