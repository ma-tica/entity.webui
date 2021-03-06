package com.mcmatica.entity.webui.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to be used to customize the setter / getter property name 
 * if different from the field name
 *  
 * @author Matteo Carminati
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface MCSelectable {
	
	/**
	 * bean method called to return the list value
	 * @return
	 */
	String value();
}
