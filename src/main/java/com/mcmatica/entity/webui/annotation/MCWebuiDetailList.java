package com.mcmatica.entity.webui.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface MCWebuiDetailList {

	/**
	 * Selection expression used to filter the list 
	 * @return
	 */
	public String selection();
	
	/**
	 * Parent selected field reference.  Default is 'selected'
	 * Usefull with multi-level detail lists
	 * 
	 * @return
	 */
	public String parentPropertyName() default "selected";
	
	/**
	 * Class annotated with MCWebui used to build the detail list user interface
	 * @return
	 */
	public Class<?> uiClass();
	
}
