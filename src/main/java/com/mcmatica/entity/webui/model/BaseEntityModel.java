package com.mcmatica.entity.webui.model;

import java.util.List;

public interface BaseEntityModel {

	String getId();
	
	void setId(String id);
	
	/**
	 * wrapper of String Id
	 * @return
	 */
	Long getIdValue();
	
	void setIdValue(Long value);
	
	boolean isNewInstance();
	
	void setNewInstanceState(boolean value);
	
	List<BaseEntityModel> getFieldListItemsRemoved(String childListName);

	void fullLoad();
	
	
}
