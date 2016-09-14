package entity.webui.provider;

import entity.webui.model.BaseEntityModel;

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
	
	public String getParentField()
	{
		return String.format("%s.%s.%s", this.parentBeanName, "selected", this.name);
	}

}
