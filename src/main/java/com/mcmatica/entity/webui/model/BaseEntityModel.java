package com.mcmatica.entity.webui.model;

public interface BaseEntityModel {

	String getId();
	
	void setId(String id);
	
	Long getIdValue();
	
	void setIdValue(Long value);
	
	boolean isNewInstance();
	
	void setNewInstanceState(boolean value);
	
	String getSelectionLabel();
	
}
