package com.mcmatica.entity.webui.service;

import java.util.List;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;

import com.mcmatica.entity.webui.bean.BaseUi;
import com.mcmatica.entity.webui.model.BaseEntityDataModel;
import com.mcmatica.entity.webui.model.BaseEntityModel;




public interface BaseWebuiService<T extends BaseEntityModel> {

	BaseEntityDataModel<T> buildList();
	
	Panel buildPanelGrid();
	
	DataTable buildSelectionGrid();
	
	Panel buildEditFormDialogs();
	
	//List<FieldModel> buildShortListFields();
	
	T getSelected();

	void save();

	

	T getById(String id);

	void delete();
	
	T create();
	
	void setDefaultValues();
	
	List<T> findAll();

	boolean isEditing();

	void cancel();

	List<T> find(String filter);
	
	long count();
	
	long count(String filter);

	void setSelected(T selected);

	
}
