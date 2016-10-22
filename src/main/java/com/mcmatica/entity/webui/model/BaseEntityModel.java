package com.mcmatica.entity.webui.model;

import java.util.List;

public interface BaseEntityModel {

	String getId();
	
	void setId(String id);
	
	Long getIdValue();
	
	void setIdValue(Long value);
	
	boolean isNewInstance();
	
	void setNewInstanceState(boolean value);
	
	String getSelectionLabel();

	List<BaseEntityModel> getFieldListItemsRemoved(String childListName);
	
}
