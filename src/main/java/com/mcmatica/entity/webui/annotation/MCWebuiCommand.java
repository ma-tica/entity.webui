package com.mcmatica.entity.webui.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public  @interface MCWebuiCommand {
	/**
	 * Expression string of the method
	 */
	public String memberExpression();


	/**
	 * Label of the command
	 */
	public String label();
	
	
	/**
	 * sequence position
	 */
	public int sequence() default 0;
	
}
