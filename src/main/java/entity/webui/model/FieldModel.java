package entity.webui.model;

public class FieldModel {

	
	public enum Controller {
		INPUT_TEXT,
		INPUT_NUMBER,
		INPUT_DATE
		;
	}
	
	
	private String caption;
	private Controller controller;
	private String propertyName;
	private String beanControllerName;
	private Class<?> clazz;
	private boolean required;
	private int shortListPosition;
	private int formPosition;
	private int colSpan;

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption
	 * 			 the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * @param controller 
	 * 			 the controller to set
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName 
	 * 		the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the beanControllerName
	 */
	public String getBeanControllerName() {
		return beanControllerName;
	}

	/**
	 * @param beanControllerName 
	 * 			the beanControllerName to set
	 */
	public void setBeanControllerName(String beanControllerName) {
		this.beanControllerName = beanControllerName;
	}

	/**
	 * @return the clazz
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required 
	 * 			the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	
	
	/**
	 * @return the shortListPosition
	 */
	public int getShortListPosition() {
		return shortListPosition;
	}

	/**
	 * @param shortListPosition the shortListPosition to set
	 */
	public void setShortListPosition(int shortListPosition) {
		this.shortListPosition = shortListPosition;
	}

	/**
	 * @return the formPosition
	 */
	public int getFormPosition() {
		return formPosition;
	}

	/**
	 * @param formPosition the formPosition to set
	 */
	public void setFormPosition(int formPosition) {
		this.formPosition = formPosition;
	}

	/**
	 * @return the colSpan
	 */
	public int getColSpan() {
		return colSpan;
	}

	/**
	 * @param colSpan the colSpan to set
	 */
	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public String getId()
	{
		return this.getPropertyName();
	}
	
}
