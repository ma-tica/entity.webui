package com.mcmatica.login.model;

import com.mcmatica.entity.webui.model.BaseEntityModel;

public interface BaseUser extends BaseEntityModel {

	public String getPassword();
	
	public void setPassword(String password);
	
	public String getUserid();
	
	public void setUserid(String userid);
	
}
