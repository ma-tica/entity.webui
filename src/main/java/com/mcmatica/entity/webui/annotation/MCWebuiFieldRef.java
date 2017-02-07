package com.mcmatica.entity.webui.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface MCWebuiFieldRef {
	
	/**
	 * BaseUi class of referenced entity
	 * @return
	 */
	public Class<?> refUiClass();
	
	/**
	 * Override the default refUiClass#findAll() method 
	 * used for SelectOneMenu controller
	 * or the refUiClass#complete(String query) method from
	 * Autocomplete controller
	 */
	public String getListExpression() default "";

}
