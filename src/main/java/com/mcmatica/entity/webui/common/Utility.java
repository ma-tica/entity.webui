package com.mcmatica.entity.webui.common;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;

import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;
import org.springframework.data.repository.query.parser.Part;

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
	public static AjaxBehavior createAjaxBehaviour(String listenerExpression, String update) {
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
	
	// Lang -----------------------------------------------------------------------------------------------------------

		/**
		 * Returns <code>true</code> if the given string is null or is empty.
		 * @param string The string to be checked on emptiness.
		 * @return <code>true</code> if the given string is null or is empty.
		 */
		public static boolean isEmpty(String string) {
			return string == null || string.isEmpty();
		}

		/**
		 * Returns <code>true</code> if the given collection is null or is empty.
		 * @param collection The collection to be checked on emptiness.
		 * @return <code>true</code> if the given collection is null or is empty.
		 */
		public static boolean isEmpty(Collection<?> collection) {
			return collection == null || collection.isEmpty();
		}

		/**
		 * Returns <code>true</code> if the given map is null or is empty.
		 * @param map The map to be checked on emptiness.
		 * @return <code>true</code> if the given map is null or is empty.
		 */
		public static boolean isEmpty(Map<?, ?> map) {
			return map == null || map.isEmpty();
		}

		/**
		 * Returns <code>true</code> if the given part is null or is empty.
		 * @param part The part to be checked on emptiness.
		 * @return <code>true</code> if the given part is null or is empty.
		 * @since 2.6
		 */
//		public static boolean isEmpty(Part part) {
//			return part == null || (isEmpty(getSubmittedFileName(part)) && part.getSize() <= 0);
//		}

		/**
		 * Returns <code>true</code> if the given object is null or an empty array or has an empty toString() result.
		 * @param value The value to be checked on emptiness.
		 * @return <code>true</code> if the given object is null or an empty array or has an empty toString() result.
		 */
		public static boolean isEmpty(Object value) {
			if (value == null) {
				return true;
			}
			else if (value instanceof String) {
				return isEmpty((String) value);
			}
			else if (value instanceof Collection) {
				return isEmpty((Collection<?>) value);
			}
			else if (value instanceof Map) {
				return isEmpty((Map<?, ?>) value);
			}
			else if (value instanceof Part) {
				return isEmpty((Part) value);
			}
			else if (value.getClass().isArray()) {
				return Array.getLength(value) == 0;
			}
			else {
				return value.toString() == null || value.toString().isEmpty();
			}
		}

		/**
		 * Returns <code>true</code> if at least one value is empty.
		 * @param values the values to be checked on emptiness
		 * @return <code>true</code> if any value is empty and <code>false</code> if no values are empty
		 * @since 1.8
		 */
		public static boolean isAnyEmpty(Object... values) {
			for (Object value : values) {
				if (isEmpty(value)) {
					return true;
				}
			}

			return false;
		}
}
