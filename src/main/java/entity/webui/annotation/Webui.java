package entity.webui.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Webui {
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
	
	
	
}
