package com.mcmatica.entity.webui.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to get a linked entity properties
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface MCLinkedField {

	/**
	 * parent field containing the reference to the linked entity
	 */
	public String parentField();
	
	/**
	 * value expression used to retrieve the properties of the linked entity
	 */
	public String valueExpression();
}
