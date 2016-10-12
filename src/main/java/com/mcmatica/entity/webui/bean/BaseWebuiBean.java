package com.mcmatica.entity.webui.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;

import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.FieldModel;
import com.mcmatica.entity.webui.service.BaseWebuiService;

//public abstract class  BaseWebuiBean<T extends BaseEntityModel> {

public  abstract class  BaseWebuiBean implements Serializable {

	
	private static final String XHTML_ROWID = "rowId";
	
	//protected BaseWebuiService<BaseEntityModel> service;
	protected BaseWebuiService service;
	
	protected List<FieldModel> columns = new ArrayList<FieldModel>();
	
	protected List<BaseEntityModel> list;
	
	protected List<BaseEntityModel> listFiltered;

	protected Panel formPanel;
	
	protected DataTable selectionGrid;
	
	protected boolean editing;
	
	public abstract void init();
	
	
/*
 * ---------------------------------
 * B I N D I N G S
 */
	


	public List<BaseEntityModel> getList() {
		if (this.list == null)
		{
			this.list = this.service.buildList();
			this.listFiltered = this.service.buildList();
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
			return true;
		}
		return false;
		//return editing;
	}

	
/*
 * ---------------------------------
 * C O M M A N D
 */
	
	
	public void save()
	{
		this.service.save();
		this.editing = false;
		this.listFiltered = null;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "save"));
	}
	
	public void create()
	{
		this.service.create();
		this.service.setDefaultValue();
		this.editing = true;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "addnew"));

	}
	
	public void delete()
	{
		this.service.delete(this.service.getSelected());
		this.editing = false;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "delete"));
		
	}
	
	public void refresh()
	{
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "refresh"));
		
	}
	

	public abstract String goToHome();


	
	
/*
 * ---------------------------------------
 * P R I V A T E	
 */
	
	
}
