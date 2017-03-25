package com.mcmatica.entity.webui.model;

import com.mcmatica.entity.webui.model.scanner.EventModel;

public class DetailListModel_old<T extends BaseEntityModel> {

	private String name;
	private String parentBeanName;
	private Class<T> propertyType;
	private EventModel event;

	
	public DetailListModel_old(String parentBeanName, String name, Class<T> propertyType, EventModel event) {
		super();
		this.parentBeanName= parentBeanName;
		this.name = name;
		this.propertyType = propertyType;
		this.event = event;
	}

	
	/**
	 * @return the parentBeanName
	 */
	public String getParentBeanName() {
		return parentBeanName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the propertyType
	 */
	public Class<T> getPropertyType() {
		return this.propertyType;
	}
	
	public String getGetterSetterValueName()
	{
		return String.format("%s.%s.%s", this.parentBeanName, "selected", this.name);
	}

	public String getAddValueMethodName()
	{
		return String.format("%s.add%s()", this.parentBeanName, this.propertyType.getSimpleName());
	}

	public String getDeleteValueMethodName(String varName)
	{
		//return String.format("%s.delete%s(%s)", this.parentBeanName, this.propertyType.getSimpleName(), varName);
		return String.format("%s.remove(%s)", getGetterSetterValueName(), varName);
	}


	public EventModel getEvent() {
		return event;
	}


	public void setEvent(EventModel event) {
		this.event = event;
	}

}
