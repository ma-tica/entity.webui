package com.mcmatica.entity.webui.bean;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;

import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.BaseEntityDataModel;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.FieldModel;
import com.mcmatica.entity.webui.service.BaseWebuiService;
import com.mcmatica.jqb.Jqb;
import com.mcmatica.jqb.JqbDialect;
import com.mcmatica.jqb.JqbWhereBuilder;


public  abstract class  BaseWebuiBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7423178114110032725L;

	
	protected BaseWebuiService service;
	
	protected List<FieldModel> columns = new ArrayList<FieldModel>();
	
	protected BaseEntityDataModel<BaseEntityModel> list;
	
	protected List<BaseEntityModel> listFiltered;
	
	protected Panel formPanel;
	
	protected DataTable selectionGrid;
	
	public abstract void init();
	
	
/*
 * ---------------------------------
 * B I N D I N G S
 */
	


	public BaseEntityDataModel<BaseEntityModel> getList() {
		if (this.list == null)
		{
			this.list = this.service.buildList();
			//this.listFiltered = this.service.buildList();
			this.listFiltered =  this.list.getData();
		}
		
		
		return this.list;
	}



	public void setList(BaseEntityDataModel<BaseEntityModel> list) {
		this.list = list;
	}
	
	public List<BaseEntityModel> getListFiltered() {
		if (this.listFiltered == null) {
			this.listFiltered = this.service.buildList().getData();
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

	
	/**
	 * Used for autocomplete components
	 * @param query
	 * @return
	 */
	public List<BaseEntityModel> complete(String query)
	{
	    String nome = "";
		if(query.contains(" - ")) {
			nome = query.substring(query.indexOf(" - ") + 3);
		}else{
			nome = query;
		}
		
//		Query qry = new Query();
//		
//		qry.addCriteria(Criteria.where("nome").regex(nome));
		
		Jqb jqb = new Jqb(JqbDialect.MONGODB);
		JqbWhereBuilder wh = jqb.where(jqb.property("nome").contains(nome));
		
		
		return this.service.find(wh.text());
		
	}

	
	public boolean isEditing() {
//		if (this.getSelected() != null)
//		{
//			return this.service.isEditing();
//		}
//		return true;
		return this.service.isEditing();
		
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
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "save"));
	}
	
	public void create()
	{
		this.service.create();
		this.service.setDefaultValues();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "addnew"));

	}
	
	public void delete()
	{
		this.service.delete();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "delete"));
		
	}
	
	public void cancel()
	{
		this.service.cancel();
		
//		BaseEntityModel e = this.list.getRowData(this.getSelected().getId());
//		e = this.getSelected();
		
		//this.setSelected(this.service.getSelected());
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "cancel"));
	}
	
	public void refresh()
	{
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "refresh"));
		
	}
	
	public void next() 
	{
		BaseEntityModel d = this.list.getRowData("000000000017");
		this.service.setSelected(d);
	}

	
	public void removeFieldListItem(String getter, String childListName, String selectionName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Method mthGetSelected = this.getClass().getMethod("get" + Utility.capitalize(selectionName) );
		BaseEntityModel itemselected;
		itemselected = (BaseEntityModel) mthGetSelected.invoke(this);
		this.removeFieldListItem(getter, childListName, itemselected);
		
	}
	
	public void removeFieldListItem(String getter, String childListName, BaseEntityModel item) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		
		/*
		 * FUNZIONA SOLO CON 2 LIVELLI AL MASSIMO ??!
		 */
		
		/*
		 * Put the removed item into the special list used for cascading save
		 */
		this.getSelected().getFieldListItemsRemoved(childListName).add(item);
		
		//Method mth = this.getSelected().getClass().getMethod("get" + Utility.capitalize(childListName) );
		
		BaseEntityModel itemselected;
		 
		if (getter != null && !getter.isEmpty()) 
		{
			Method mthGetSelected = this.getClass().getMethod("get" + Utility.capitalize(getter) );
			itemselected = (BaseEntityModel) mthGetSelected.invoke(this);
		}else {
			itemselected = this.getSelected();
		}
		Method mth = itemselected.getClass().getMethod("get" + Utility.capitalize(childListName) );
		List<BaseEntityModel> list =  (List<BaseEntityModel>) mth.invoke(itemselected);
		list.remove(item);
		
		/*
		 * Update the User interface
		 */
		this.updateUI();
	}

	
	
	
	public void onChangeField(AjaxBehaviorEvent event)
	{
		this.updateUI();
	}
	

	/**
	 * Used to refresh the user interface
	 * 
	 * TODO : migliorare !!!!!!!
	 */
	private void updateUI()
	{
		RequestContext.getCurrentInstance().update("form_toolbar");
		//RequestContext.getCurrentInstance().update("form_grid");
	}
	
	public abstract String goToHome();


	
	
/*
 * ---------------------------------------
 * P R I V A T E	
 */
	
	
}
