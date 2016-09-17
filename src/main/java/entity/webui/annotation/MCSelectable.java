package entity.webui.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface MCSelectable {
	
	/**
	 * bean method called to return the list value
	 * @return
	 */
	String value();
}
