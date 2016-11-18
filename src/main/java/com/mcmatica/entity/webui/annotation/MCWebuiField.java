package com.mcmatica.entity.webui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mcmatica.entity.webui.model.FieldModel;

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
	
	
	
}
