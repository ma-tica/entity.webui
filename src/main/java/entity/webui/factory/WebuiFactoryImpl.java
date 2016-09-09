package entity.webui.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;

import entity.webui.annotation.Webui;
import entity.webui.model.FieldModel;

public class WebuiFactoryImpl implements WebuiFactory {

	Class<?> clazz;
	
	List<FieldModel> fields;
	
	String beanName;
	
	/**
	 * Constructor
	 * 
	 * @param clazz
	 * @param beanName
	 */
	public WebuiFactoryImpl(Class<?> clazz, String beanName)
	{
		this.clazz = clazz;
		this.beanName = beanName;
		this.fillFieldList(clazz, beanName);
		
	}
	
	@Override
	public HtmlPanelGrid buildPanelGrid()
	{
		HtmlPanelGrid panel = new HtmlPanelGrid();
		panel.setColumns(4);
		
		for(FieldModel field : this.fields)
		{
			switch (field.getController()) {
			case INPUT_TEXT:
				this.buildInputText(field, panel);
				break;

			default:
				break;
			}
		}
		
		return panel;
	}
	
	
	@Override
	public List<FieldModel> getFields()
	{
		return this.fields;
	}
	
	
	
/*
 * --------------------------
 * P R I V A T E	
 */
	private void fillFieldList(Class<?> clazz, String beanControllerName)
	{
		this.fields = new ArrayList<FieldModel>();
		for (Field field : clazz.getDeclaredFields())
		{
			Webui webui = field.getAnnotation(Webui.class);
			if (webui != null)
			{
				FieldModel fmodel = new FieldModel();
				fmodel.setCaption(webui.caption());
				fmodel.setController(webui.controller());
				fmodel.setPropertyName(field.getName());
				fmodel.setBeanControllerName(beanControllerName);
				fmodel.setClazz(field.getType());
				this.fields.add(fmodel);				
			}
		}
	}
	
	private void buildInputText(FieldModel field, HtmlPanelGrid panel)
	{
		InputText input = new InputText();
		OutputLabel label = new OutputLabel();
					
		label.setValue(field.getCaption());
		label.setFor(input.getId());
		
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ValueExpression valueEx = app.getExpressionFactory().createValueExpression(context.getELContext(), this.elValue(field), field.getClazz());
		input.setValueExpression("value", valueEx);
		
		panel.getChildren().add(label);
		panel.getChildren().add(input);
	}
	
	private String elValue(FieldModel fmodel)
	{
		String el = "#{%s.%s.%s}";
		return String.format(el, fmodel.getBeanControllerName(), "selected", fmodel.getPropertyName());
	}
}
