package entity.webui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import entity.webui.model.FieldModel;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Webui {

	public String caption() default "";
	
	public FieldModel.Controller controller() default FieldModel.Controller.INPUT_TEXT; 
	
	public boolean required() default false;
	
	
}
