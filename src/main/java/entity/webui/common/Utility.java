package entity.webui.common;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public class Utility {

	/**
	 * Shortcut to create JSF expression
	 * 
	 * @param stringExpression
	 * @param type
	 * @return
	 */
	public static ValueExpression createValueExp(String stringExpression, Class<?> type)
	{
		FacesContext context = FacesContext.getCurrentInstance();		
		return context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), stringExpression, type);		
	}


	public static MethodExpression createMethodExp(String stringExpression)
	{
		FacesContext context = FacesContext.getCurrentInstance();		
		return context.getApplication().getExpressionFactory().createMethodExpression(Utility.getELContext(), stringExpression,
																			Void.class, new Class[]{});
	}

	public static MethodExpression createMethodExp(String stringExpression, Class[] params)
	{
		FacesContext context = FacesContext.getCurrentInstance();		
		return context.getApplication().getExpressionFactory().createMethodExpression(Utility.getELContext(), stringExpression,
																			Void.class, params);
	}
	
	/**
	 * Shortucut to get faces espression context
	 * @return
	 */
	public static ELContext getELContext()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getELContext();
	}
}
