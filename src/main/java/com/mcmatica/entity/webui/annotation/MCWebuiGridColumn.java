package com.mcmatica.entity.webui.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface MCWebuiGridColumn {

	public String dbFieldName() default "";
	
	public int shortListPosition() ;

	public String caption() default "";
		
	//public String width() default "100%";
	
}
