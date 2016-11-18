package com.mcmatica.entity.webui.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;
import org.springframework.data.mongodb.core.query.Query;

import com.mcmatica.entity.webui.model.BaseEntityDataModel;
import com.mcmatica.entity.webui.model.BaseEntityModel;



//public interface BaseWebuiService<T extends BaseEntityModel> {
public interface BaseWebuiService {

	<T extends BaseEntityModel> BaseEntityDataModel<T> buildList();
	
	Panel buildPanelGrid();
	
	DataTable buildSelectionGrid();
	
	//List<FieldModel> buildShortListFields();
	
	<T extends BaseEntityModel> T getSelected();

	void save();

	<T extends BaseEntityModel> void setSelected(T selected);

	<T extends BaseEntityModel> T getById(String id);

	void delete();
	
	<T extends BaseEntityModel> T create();
	
	void setDefaultValues();
	
	<T extends BaseEntityModel> List<T> findAll();

	boolean isEditing();

	void cancel();

	<T extends BaseEntityModel> List<T> find(Query query);
	
}
