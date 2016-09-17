package entity.webui.common;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;

import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;

public class Utility {

	/**
	 * Shortcut to create JSF expression with parameters
	 * 
	 * @param stringExpression
	 * @param type
	 * @return
	 */
	public static ValueExpression createExpression(String stringExpression, Class<?> type) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(),
				stringExpression, type);
	}

	/**
	 * Shortcut to create JSF method expression without parameters
	 * 
	 * @param stringExpression
	 * @return
	 */
	public static MethodExpression createMethodExp(String stringExpression) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().getExpressionFactory().createMethodExpression(Utility.getELContext(),
				stringExpression, Void.class, new Class[] {});
	}

	/**
	 * Shortcut to create JSF method expression with parameters
	 * 
	 * @param stringExpression
	 * @param params
	 * @return
	 */
	public static MethodExpression createMethodExp(String stringExpression, Class[] params) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().getExpressionFactory().createMethodExpression(Utility.getELContext(),
				stringExpression, Void.class, params);
	}

	/**
	 * Create a value expression if stringExpression has the format #{...}
	 * 
	 * @param stringExpression
	 * @param type
	 * @return
	 */
	public static <T extends Object> T createValue(String stringExpression, Class<T> type) {

		if (stringExpression.startsWith("#{")) {
			ValueExpression valueExCaption = Utility.createExpression(stringExpression, type);
			return (T) valueExCaption.getValue(Utility.getELContext());
		} else {
			if (type.getName().equals("boolean") || type.getName().equals("Boolean")) {
				if (stringExpression.equals("false")) {
					return (T) Boolean.FALSE;
				} else if (stringExpression.equals("true")) {
					return (T) Boolean.TRUE;
				} else {
					return (T) stringExpression;
				}
			}
			return (T) stringExpression;
		}
	}

	/**
	 * Create an Ajax behaviour client method
	 * 
	 * @param listenerExpression
	 * @return
	 */
	public static AjaxBehavior createAjaxBejhaviour(String listenerExpression, String update) {
		FacesContext context = FacesContext.getCurrentInstance();
		AjaxBehavior ajaxBehavior = (AjaxBehavior) context.getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
		if (listenerExpression != null && !listenerExpression.isEmpty()) {

			MethodExpression me = context.getApplication().getExpressionFactory().createMethodExpression(
					context.getELContext(), listenerExpression, null, new Class<?>[] { BehaviorEvent.class });
			ajaxBehavior.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(me, me));
		}
		ajaxBehavior.setProcess("@this");
		ajaxBehavior.setUpdate(update);
		return ajaxBehavior;
	}

	/**
	 * Shortucut to get faces espression context
	 * 
	 * @return
	 */
	public static ELContext getELContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getELContext();
	}

	/**
	 * Capitalize the first char of a string
	 * 
	 * @param text
	 * @return
	 */
	public static String capitalize(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}
}
