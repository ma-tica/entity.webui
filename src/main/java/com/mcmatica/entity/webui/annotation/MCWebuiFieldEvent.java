package com.mcmatica.entity.webui.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface MCWebuiFieldEvent {
	
	public String event();
	
	public String update() default "";
	
	public String listener() default "";
	
	

}
