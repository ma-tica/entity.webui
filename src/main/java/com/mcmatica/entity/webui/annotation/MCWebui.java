package com.mcmatica.entity.webui.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public  @interface MCWebui {
	/**
	 * number of columns of HtmlPanelGrid
	 * @return
	 */
	public int panelColumns();

	/**
	 * Jsf id of panel Grid
	 */
	//public String panelId();
	
	/**
	 * name of the main primefaces bean controller that extends 
	 */
	public String beanControllerName() ;
	
	
	/**
	 * Header of panel
	 * @return
	 */
	public String title() default "";

	/**
	 * Class of entity managed by the user interface
	 * @return
	 */
	public Class<?> entityClass();
	
//	/**
//	 * set which jsf component update when the selected item enter in status 'editing'
//	 */
//	public String updateOnEditing() default "";
	
	/**
	 * Primefaces PanelGrid columnClasses attribute
	 * @return
	 */
	public String columnClasses() default "";
	
	
	/**
	 * Custom commands
	 * @return
	 */
	public MCWebuiCommand[] commands() default {};
	
	
}
