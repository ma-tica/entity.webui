package com.mcmatica.login.model;

import java.util.List;

import com.mcmatica.entity.webui.model.BaseEntityModel;

public interface BaseUser extends BaseEntityModel {

	public String getPassword();
	
	public void setPassword(String password);
	
	public String getUserid();
	
	public void setUserid(String userid);
	
	public <T extends BaseRole> void setRoles(List<T> roles);
	
	public <T extends BaseRole> List<T> getRoles();
	
}
