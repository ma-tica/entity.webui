package entity.webui.model;

public class FieldModel {

	
	public enum EditorComponent {
		UNDEFINED,
		INPUT_TEXT,
		INPUT_NUMBER,
		INPUT_DATE,
		SELECTION_ONE_MENU
		;
	}
	
	
	private String caption;
	private EditorComponent editorComponent;
	private String propertyName;
	private String beanControllerName;
	private Class<?> propertyType;
	private boolean required;
	private int shortListPosition;
	private int formPosition;
	private int colSpan;
	
	/**
	 * Controller bean if field is a BaseModelEntity object
	 * It's need for editor component like SelecetOneMenu 
	 */
	private String relatedBeanControllerName;
	
	
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
	public EditorComponent getEditorComponent() {
		return editorComponent;
	}

	/**
	 * @param controller 
	 * 			 the controller to set
	 */
	public void setEditorComponent(EditorComponent editorComponent) {
		this.editorComponent = editorComponent;
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
	 * @return the propertyType
	 */
	public Class<?> getPropertyType() {
		return propertyType;
	}

	/**
	 * @param propertyType the propertyType to set
	 */
	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
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


	/**
	 * @return the relatedBeanControllerName
	 */
	public String getRelatedBeanControllerName() {
		return relatedBeanControllerName;
	}

	/**
	 * @param relatedBeanControllerName the relatedBeanControllerName to set
	 */
	public void setRelatedBeanControllerName(String relatedBeanControllerName) {
		this.relatedBeanControllerName = relatedBeanControllerName;
	}

	public String getId()
	{
		return this.getPropertyName();
	}
	
}
