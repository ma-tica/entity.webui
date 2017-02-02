package com.mcmatica.entity.webui.common;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;
import org.springframework.aop.support.AopUtils;

import com.mcmatica.entity.webui.annotation.MCDbRef;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.service.LoadLazyProvider;

public class Utility {

	private static Logger logger = LogManager.getLogger(Utility.class.getName());
	
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

	public static MethodExpression createGetterMethodExp(String stringExpression, Class[] params, Class<?> returnedType) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().getExpressionFactory().createMethodExpression(Utility.getELContext(),
				stringExpression, returnedType, params);
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

	public static AjaxBehavior createClientBehaviour(String onstart, String onsuccess, String update) {
		FacesContext context = FacesContext.getCurrentInstance();
		AjaxBehavior ajaxBehavior = (AjaxBehavior) context.getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
		ajaxBehavior.setOnstart(onstart);
		ajaxBehavior.setOnsuccess(onsuccess);
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

	/**
	 * Create a new BaseEntity Object cloning an existing one
	 * 
	 * @param original
	 * @return
	 * @throws Exception
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static <T extends BaseEntityModel> T cloneEntity(T original) throws Exception {

		/*
		 *  AspectJ management
		 */
		T original0 = null;
		if (AopUtils.isJdkDynamicProxy(original))
		{
			/*
			 * obtain the target object behind the Proxy
			 */
			original0 = (T) ((org.springframework.aop.framework.Advised)original).getTargetSource().getTarget();
		}else
		{
			original0 = original;
		}


		
		
		if (original0 == null){
			return null;
		}
		T cloned = null;
		try {
			cloned = (T) Class.forName(original0.getClass().getName()).newInstance();
			for (Field field : original0.getClass().getDeclaredFields()) {
				if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					field.setAccessible(true);
					Field clonedField = cloned.getClass().getDeclaredField(field.getName());
					clonedField.setAccessible(true);

					if (field.getType().equals(List.class)) {
						List<T> originalListItems = (List<T>) field.get(original0);
						List<T> clonedListItems = null;
						if (originalListItems != null) {
							clonedListItems = new ArrayList<T>();
							for (int i = 0; i < originalListItems.size(); i++) {
								T originalListItem = originalListItems.get(i);
								clonedListItems.add(cloneEntity(originalListItem));
							}
						}
						clonedField.set(cloned, clonedListItems);	
					} else {
						clonedField.set(cloned, field.get(original0));
					}
				}
			}
		} catch (InstantiationException e) {
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			throw new Exception(e);
		} catch (ClassNotFoundException e) {
			throw new Exception(e);
		} catch (NoSuchFieldException e) {
			throw new Exception(e);
		} catch (SecurityException e) {
			throw new Exception(e);
		}

		return cloned;
	}

	public static <T extends BaseEntityModel> void copyEntity(T original, T copied) throws Exception {
		/*
		 *  AspectJ management
		 */
		T original0 = null;
		if (AopUtils.isJdkDynamicProxy(original))
		{
			/*
			 * obtain the target object behind the Proxy
			 */
			original0 = (T) ((org.springframework.aop.framework.Advised)original).getTargetSource().getTarget();
		}else
		{
			original0 = original;
		}

		
		if (original0 == null) {
			return;
		}
		for (Field field : original0.getClass().getDeclaredFields()) {
			if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				field.setAccessible(true);
				Field copiedField = copied.getClass().getDeclaredField(field.getName());
				copiedField.setAccessible(true);

				if (field.getType().equals(List.class)) {
					List<T> originalListItems = (List<T>) field.get(original0);
					List<T> copiedListItems = null;
					if (originalListItems != null) {
						copiedListItems = new ArrayList<T>();
						for (int i = 0; i < originalListItems.size(); i++) {
							T originalListItem = originalListItems.get(i);
							copiedListItems.add(cloneEntity(originalListItem));
						}
					}
					copiedField.set(copied, copiedListItems);	
				} else {
					copiedField.set(copied, field.get(original0));
				}
			}
		}
		
	}
	
	
	/**
	 * Look for different field values between tow BaseEntitModel object
	 * 
	 * @param first
	 * @param second
	 * @return
	 * @throws Exception
	 */
	public static <T extends BaseEntityModel> boolean areEquals(T first0, T second0) throws Exception {
		T first = null;
		if (AopUtils.isJdkDynamicProxy(first0))
		{
			/*
			 * obtain the target object behind the Proxy
			 */
			first = (T) ((org.springframework.aop.framework.Advised)first0).getTargetSource().getTarget();
		}else
		{
			first = first0;
		}

		
		T second = null;
		if (AopUtils.isJdkDynamicProxy(second0))
		{
			/*
			 * obtain the target object behind the Proxy
			 */
			second = (T) ((org.springframework.aop.framework.Advised)second0).getTargetSource().getTarget();
		}else
		{
			second = second0;
		}

		
		if (first == null && second == null) {			
			return true;
		}
		if (first == null && second != null) {
			logger.info(String.format("1-[areEquals -> false]  %s <> %s", first+"", second+"" )); 
			return false;
		}
		if (first != null && second == null) {
			logger.info(String.format("2-[areEquals -> false]  %s <> %s", first+"", second+"" )); 
			return false;
		}
		if (first.getClass().getName().equals(second.getClass().getName())) {
			// List<String> differences = new ArrayList<String>();
			for (Field field : first.getClass().getDeclaredFields()) {
				if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					field.setAccessible(true);
					Field otherField = second.getClass().getDeclaredField(field.getName());
					otherField.setAccessible(true);

					if (field.getType().equals(List.class)) {
						// Type elementType = ((ParameterizedType)
						// field.getGenericType()).getActualTypeArguments()[0];
						List<T> firstListItems = (List<T>) field.get(first);
						List<T> secondListItems = (List<T>) otherField.get(second);
						if (firstListItems == null && secondListItems == null) {
							return true;
						} else if (firstListItems == null && secondListItems != null) {
							logger.info(String.format("3-[areEquals -> false] field:%s  %s <> %s", field.getName(), firstListItems+"", secondListItems+"" )); 
							return false;
						} else if (firstListItems != null & secondListItems == null) {
							logger.info(String.format("4-[areEquals -> false] field:%s  %s <> %s", field.getName(), firstListItems+"", secondListItems+"" )); 
							return false;
						} else if (firstListItems.size() != secondListItems.size())	{
							logger.info(String.format("5-[areEquals -> false] field:%s  size %d <> %d", field.getName(), firstListItems.size(), secondListItems.size() )); 
							return false;
						} else {
							
							
							for (int i = 0; i < firstListItems.size(); i++) {
								T firtsListItem = firstListItems.get(i);
								T secondListItem = secondListItems.get(i);
								if (!(areEquals(firtsListItem, secondListItem))) {
									logger.info(String.format("6-[areEquals -> false] field:%s  %s <> %s", field.getName(), firtsListItem+"", secondListItem+"" )); 
									return false;
								}
							}
						}
					} else {

						Object firstFieldValue = field.get(first);
						Object otherFieldValue = otherField.get(second);
						if (firstFieldValue == null && otherFieldValue != null) {
							logger.info(String.format("7-[areEquals -> false] field:%s  %s <> %s", field.getName(), firstFieldValue+"", otherFieldValue+"" )); 
							return false;
						} else if (firstFieldValue != null && otherFieldValue == null) {
							logger.info(String.format("8-[areEquals -> false] field:%s  %s <> %s", field.getName(), firstFieldValue+"", otherFieldValue+"" )); 
							return false;
						} else if (firstFieldValue != null && otherFieldValue != null
								&& !firstFieldValue.equals(otherFieldValue)) {
							logger.info(String.format("9-[areEquals -> false] field:%s  %s <> %s", field.getName(), firstFieldValue+"", otherFieldValue+"" )); 
							return false;
						}
					}

				}
			}
			
			
			
			return true;
		} else {
			throw new Exception("Cannot get differences from objects of diferent classes ");
		}
	}
	
	
	 
	public static String randomId(int length) {
	      Random rnd = new Random();	
	      int number = rnd.nextInt();
	      
	      return String.format("%0" + length + "d", Math.abs(number) * -1).substring(0, length);
		
	}


	public static <T extends BaseEntityModel> void loadLazyEntityProperties(T entity) throws Exception {
		/*
		 *  AspectJ management
		 */
		T original0 = null;
		if (AopUtils.isJdkDynamicProxy(entity))
		{
			/*
			 * obtain the target object behind the Proxy
			 */
			original0 = (T) ((org.springframework.aop.framework.Advised)entity).getTargetSource().getTarget();
		}else
		{
			original0 = entity;
		}

		
		if (original0 == null) {
			return;
		}
		for (Field field : original0.getClass().getDeclaredFields()) {
			if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				field.setAccessible(true);
				MCDbRef mcdbref = field.getAnnotation(MCDbRef.class);
				if (mcdbref != null && mcdbref.lazy())
				{
					if (field.get(original0) == null)
					{
						
						
						if (field.getType().equals(List.class)) {
							
							ParameterizedType itemListType = (ParameterizedType) field.getGenericType();
					        Class<T> itemListClass = (Class<T>) itemListType.getActualTypeArguments()[0];
							
					        List<T> originalListItems = LoadLazyProvider.istance.listLoadLazy(itemListClass, original0, field.getName());

					        field.set(original0, originalListItems);
					        
							if (originalListItems != null) {
								
								for (int i = 0; i < originalListItems.size(); i++) {
									T originalListItem = originalListItems.get(i);
									loadLazyEntityProperties(originalListItem);									
								}
							}
						}else{
							LoadLazyProvider.istance.listLoadLazy(original0.getClass(), original0, field.getName());
						}
					}			
				}
			}
		}
		
	}
	
	
	
	// Lang
	// -----------------------------------------------------------------------------------------------------------

	/**
	 * Returns <code>true</code> if the given string is null or is empty.
	 * 
	 * @param string
	 *            The string to be checked on emptiness.
	 * @return <code>true</code> if the given string is null or is empty.
	 */
	public static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	/**
	 * Returns <code>true</code> if the given collection is null or is empty.
	 * 
	 * @param collection
	 *            The collection to be checked on emptiness.
	 * @return <code>true</code> if the given collection is null or is empty.
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Returns <code>true</code> if the given map is null or is empty.
	 * 
	 * @param map
	 *            The map to be checked on emptiness.
	 * @return <code>true</code> if the given map is null or is empty.
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * Returns <code>true</code> if the given part is null or is empty.
	 * 
	 * @param part
	 *            The part to be checked on emptiness.
	 * @return <code>true</code> if the given part is null or is empty.
	 * @since 2.6
	 */
	// public static boolean isEmpty(Part part) {
	// return part == null || (isEmpty(getSubmittedFileName(part)) &&
	// part.getSize() <= 0);
	// }

	/**
	 * Returns <code>true</code> if the given object is null or an empty array
	 * or has an empty toString() result.
	 * 
	 * @param value
	 *            The value to be checked on emptiness.
	 * @return <code>true</code> if the given object is null or an empty array
	 *         or has an empty toString() result.
	 */
	public static boolean isEmptyOrNull(Object value) {
		if (value == null) {
			return true;
		} else if (value instanceof String) {
			return isEmpty((String) value);
		} else if (value instanceof Collection) {
			return isEmpty((Collection<?>) value);
		} else if (value instanceof Map) {
			return isEmpty((Map<?, ?>) value);
			// } else if (value instanceof Part) {
			// return isEmpty((Part) value);
		} else if (value.getClass().isArray()) {
			return Array.getLength(value) == 0;
		} else {
			return value.toString() == null || value.toString().isEmpty();
		}
	}

	/**
	 * Returns <code>true</code> if at least one value is empty.
	 * 
	 * @param values
	 *            the values to be checked on emptiness
	 * @return <code>true</code> if any value is empty and <code>false</code> if
	 *         no values are empty
	 * @since 1.8
	 */
	public static boolean isAnyEmptyOrNull(Object... values) {
		for (Object value : values) {
			if (isEmptyOrNull(value)) {
				return true;
			}
		}

		return false;
	}
	
	
}
