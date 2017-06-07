package com.mcmatica.entity.webui.service;

import java.util.List;

import com.mcmatica.entity.webui.model.BaseEntityDataModel;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.scanner.FieldModel;

public interface BaseDataService<T extends BaseEntityModel> {

	
	void save();		/* CRUD operations */
	
	void delete();		/* CRUD operations */

	T create();			/* CRUD operations */
	
	void setSelected(T selected);
	
	T getSelected();

	T getById(String id);
	
	BaseEntityDataModel<T> buildList();
		
	List<T> findAll();
	
	List<T> find(String filter);
	
	List<T> findSorted(String filter, List<String> properties);
	
	List<T> findAllSorted(List<String> properties);

	
	
//	long count();
//	
//	long count(String filter);
	
	void setDefaultValues(List<FieldModel> fieldModels);
	
	boolean isEditing();

	void cancel();

	void startEditing();

}
