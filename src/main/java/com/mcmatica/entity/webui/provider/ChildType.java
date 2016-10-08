package com.mcmatica.entity.webui.provider;

import com.mcmatica.entity.webui.model.BaseEntityModel;

public class ChildType<T extends BaseEntityModel> {

	private String name;
	private String parentBeanName;
	private Class<T> propertyType;
	
	public ChildType(String parentBeanName, String name, Class<T> propertyType) {
		super();
		this.parentBeanName= parentBeanName;
		this.name = name;
		this.propertyType = propertyType;
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

}
