package com.mcmatica.entity.webui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mcmatica.entity.webui.model.scanner.FieldModel;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MCWebuiField {
	
	

	public String caption() default "";
	
	public FieldModel.EditorComponent editorComponent() default FieldModel.EditorComponent.UNDEFINED; 
	
	public String required() default "false";
		
	public int formPosition() default 0;
	
	public int colSpan() default 1;
	
	public String defaultValue() default "";
	
	public String visible() default "true";
	
	public String readonly() default "false";
	
	public String width() default "";
	
	//public String valueExpression() default "";
	
	/**
	 * Mark the field as parte of the caption shown in the SelectionOneMenu controller or 
	 * Autocomplete controller
	 * Each field value are separated by ' - '
	 * @return
	 */
	public int selectionFieldSequence() default 0;
	
	public String mask() default "";
	
}
