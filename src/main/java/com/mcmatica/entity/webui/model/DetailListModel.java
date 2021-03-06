package com.mcmatica.entity.webui.model;

import com.mcmatica.entity.webui.model.scanner.EventModel;

public class DetailListModel {
	
	private String selectionName;
	private String parentPropertyName;
	private String propertyName;
	private String beanControllerName;
	private Class<?> propertyType;
	private Class<?> UiClassType;
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
		return String.format("%s.%s.%s", this.beanControllerName, this.parentPropertyName, this.propertyName);
	}
	
	public String getAddValueMethodName()
	{
		return String.format("%s.add%s()", this.beanControllerName, this.propertyType.getSimpleName());
	}

	public String getDeleteValueMethodName(String varName)
	{
//		return String.format("%s.removeFieldListItem('%s', %s)", this.beanControllerName, this.propertyName, varName );
		return String.format("%s.removeFieldListItem('%s', '%s', %s)", this.beanControllerName, this.parentPropertyName, this.propertyName, varName );
	}

	public String getDeleteValueMethodName()
	{
		return String.format("%s.removeFieldListItem('%s', '%s', '%s')", this.beanControllerName, this.parentPropertyName, 
																	   this.propertyName, this.selectionName  );
	}

	
//	public String getChangeEventExpression()
//	{
//		return String.format("#{%s.onChangeField}", this.beanControllerName);
//	}

	public String getParentPropertyName() {
		return parentPropertyName;
	}

	public void setParentPropertyName(String parentPropertyName) {
		this.parentPropertyName = parentPropertyName;
	}

	public Class<?> getUiClassType() {
		return UiClassType;
	}

	public void setUiClassType(Class<?> uiClassType) {
		UiClassType = uiClassType;
	}


}
