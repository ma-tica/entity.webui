package com.mcmatica.entity.webui.model;

public class FieldModel {

	
	public enum EditorComponent {
		UNDEFINED,
		INPUT_TEXT,
		INPUT_NUMBER,
		INPUT_DATE,
		INPUT_DATETIME,
		INPUT_TEXTAREA,
		SELECTION_ONE_MENU,
		BOOLEAN_CHECKBOX,
		AUTOCOMPLETE
		;
	}
	
	
	private String caption;
	private EditorComponent editorComponent;
	private String propertyName;
	private String beanControllerName;
	private Class<?> propertyType;
	private String requiredExpression;
	private int shortListPosition;
	private int formPosition;
	private int colSpan;
	private String defaultValue;
	private String visibleExpression;
	private String readonlyExpression;
	private String fillSelectionListExpression;
	private EventModel event;
	private String gridCaption;
	private String width;
	
	private String linkedParentField;
	private String linkedValueExpression;
	
	private boolean formField;
	private boolean shortGridField;
	

	
	/**
	 * Controller bean if field is a BaseModelEntity object
	 * It's need for editor component like SelecetOneMenu 
	 */
	private String relatedBeanControllerName;
	
	
	/**
	 * @return the caption
	 */
	public String getCaption() {
		if (caption == null || caption.isEmpty())
		{
			return propertyName;
		}
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

	public String getRequiredExpression() {
		return requiredExpression;
	}

	public void setRequiredExpression(String requiredExpression) {
		this.requiredExpression = requiredExpression;
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

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

//	/**
//	 * @return the inputEditorController
//	 */
//	public UIInput getInputEditorController() {
//		return inputEditorController;
//	}
//
//	/**
//	 * @param inputEditorController the inputEditorController to set
//	 */
//	public void setInputEditorController(UIInput inputEditorController) {
//		this.inputEditorController = inputEditorController;
//	}

	/**
	 * @return the visibleExpression
	 */
	public String getVisibleExpression() {
		return visibleExpression;
	}

	/**
	 * @param visibleExpression the visibleExpression to set
	 */
	public void setVisibleExpression(String visibleExpression) {
		this.visibleExpression = visibleExpression;
	}

	public String getReadonlyExpression() {
		return readonlyExpression;
	}

	public void setReadonlyExpression(String readonlyExpression) {
		this.readonlyExpression = readonlyExpression;
	}

	public String getFillSelectionListExpression() {
		if (this.fillSelectionListExpression != null) {
			return this.fillSelectionListExpression;
		}
		
		if (this.getEditorComponent().equals(EditorComponent.SELECTION_ONE_MENU)) {
			return String.format("#{%s.%s}", this.getRelatedBeanControllerName(), "findAll()");
		}else if(this.getEditorComponent().equals(EditorComponent.AUTOCOMPLETE)) {
			return String.format("#{%s.%s}", this.getRelatedBeanControllerName(), "complete");
		}
		
		return null;
	}

	public void setFillSelectionListExpression(String fillSelectionListExpression) {
		this.fillSelectionListExpression = fillSelectionListExpression;
	}

	public String getId()
	{
		return this.getPropertyName();
	}

//	public String getEventUpdateExpression() {
//		return eventUpdateExpression;
//	}
//
//	public void setEventUpdateExpression(String eventUpdateExpression) {
//		this.eventUpdateExpression = eventUpdateExpression;
//	}
//
//	public String getEventListenerExpression() {
//		return eventListenerExpression;
//	}
//
//	public void setEventListenerExpression(String eventListenerExpression) {
//		this.eventListenerExpression = eventListenerExpression;
//	}

	public EventModel getEvent() {
		return event;
	}

	public void setEvent(EventModel event) {
		this.event = event;
	}
	
	public String getChangeEventExpression()
	{
		return String.format("#{%s.onChangeField}", this.beanControllerName);
	}

	public String getGridCaption() {
		if (gridCaption == null || gridCaption.isEmpty())
		{
			return this.getCaption();
		}
		return gridCaption;
		
	}

	public void setGridCaption(String gridCaption) {
		this.gridCaption = gridCaption;
	}

//	public String getGridWidth() {
//		return gridWidth;
//	}
//
//	public void setGridWidth(String gridWidth) {
//		this.gridWidth = gridWidth;
//	}

	public boolean isFormField() {
		return formField;
	}

	public void setFormField(boolean formField) {
		this.formField = formField;
	}

	public boolean isShortGridField() {
		return shortGridField;
	}

	public void setShortGridField(boolean shortGridField) {
		this.shortGridField = shortGridField;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getLinkedParentField() {
		return linkedParentField;
	}

	public void setLinkedParentField(String linkedParentField) {
		this.linkedParentField = linkedParentField;
	}

	public String getLinkedValueExpression() {
		return linkedValueExpression;
	}

	public void setLinkedValueExpression(String linkedValueExpression) {
		this.linkedValueExpression = linkedValueExpression;
	}
}
