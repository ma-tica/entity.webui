package com.mcmatica.entity.webui.model;

public class DetailListModel {
	
	private String selectionName;
	private String parentProperyName;
	private String propertyName;
	private String beanControllerName;
	private Class<?> propertyType;
	private EventModel event;
	
	public String getSelection() {
		return String.format("%s.%s", this.beanControllerName, this.selectionName);
	}

	public void setSelectionName(String selection) {
		this.selectionName = selection;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getBeanControllerName() {
		return beanControllerName;
	}

	public void setBeanControllerName(String beanControllerName) {
		this.beanControllerName = beanControllerName;
	}

	public Class<?> getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}

	public EventModel getEvent() {
		return event;
	}

	public void setEvent(EventModel event) {
		this.event = event;
	}

	
	/*
	 * --
	 */
	
	public String getGetterSetterValueName()
	{
		return String.format("%s.%s.%s", this.beanControllerName, this.parentProperyName, this.propertyName);
	}
	
	public String getAddValueMethodName()
	{
		return String.format("%s.add%s()", this.beanControllerName, this.propertyType.getSimpleName());
	}

	public String getDeleteValueMethodName(String varName)
	{
		//return String.format("%s.delete%s(%s)", this.parentBeanName, this.propertyType.getSimpleName(), varName);
		return String.format("%s.remove(%s)", this.getGetterSetterValueName(), varName);
	}

	public String getParentProperyName() {
		return parentProperyName;
	}

	public void setParentProperyName(String parentProperyName) {
		this.parentProperyName = parentProperyName;
	}


}