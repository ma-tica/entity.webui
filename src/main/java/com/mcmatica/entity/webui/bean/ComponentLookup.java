package com.mcmatica.entity.webui.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

import com.mcmatica.entity.webui.model.BaseEntityModel;

@ManagedBean(name="component_lookup")
@RequestScoped
public class ComponentLookup {
	
	
	private BaseEntityModel tempselection;

    public BaseEntityModel getTempselection() {
		return tempselection;
	}

	public void setTempselection(BaseEntityModel tempselection) {
		this.tempselection = tempselection;
	}

   
    
    
    
}