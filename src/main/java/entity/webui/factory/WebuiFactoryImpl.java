package entity.webui.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;

import entity.webui.annotation.Webui;
import entity.webui.model.BaseEntityModel;
import entity.webui.model.FieldModel;

public class WebuiFactoryImpl<T extends BaseEntityModel> implements WebuiFactory {

	Class<T> clazz;
	
	List<FieldModel> fields;
	
	String beanName;
	
	/**
	 * Constructor
	 * 
	 * @param clazz
	 * @param beanName
	 */
	public WebuiFactoryImpl(Class<T> clazz, String managedBeanName)
	{
		this.clazz = clazz;
		this.beanName = managedBeanName;
		this.fillFieldList(clazz, beanName);
		
	}
	
	@Override
	public HtmlPanelGrid buildPanelGrid()
	{
		HtmlPanelGrid panel = new HtmlPanelGrid();
		panel.setColumns(6);
		
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
	public List<FieldModel> buildFields()
	{
		return this.fields;
	}
	
	
	
/*
 * --------------------------
 * P R I V A T E	
 */
	private void fillFieldList(Class<T> clazz, String beanControllerName)
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
				fmodel.setRequired(webui.required());
				this.fields.add(fmodel);				
			}
		}
	}
	
	private void buildInputText(FieldModel field, HtmlPanelGrid panel)
	{
		/*
		 * Input text box
		 */
		InputText input = new InputText();				
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ValueExpression valueEx = app.getExpressionFactory().createValueExpression(context.getELContext(), this.elValue(field), field.getClazz());
		input.setValueExpression("value", valueEx);
		input.setId(field.getPropertyName());
		input.setRequired(field.isRequired());
					
		/*
		 * Label
		 */
		OutputLabel label = new OutputLabel();
		label.setValue(field.getCaption());
		label.setFor(input.getId());
		
		/*
		 * message validation
		 */
		Message message = new Message();
		message.setFor(input.getId());
		message.setDisplay("icon");
		
		
		/*
		 * add to form panel 
		 */
		panel.getChildren().add(label);
		panel.getChildren().add(input);
		panel.getChildren().add(message);
	}
	
	private String elValue(FieldModel fmodel)
	{
		String el = "#{%s.%s.%s}";
		return String.format(el, fmodel.getBeanControllerName(), "selected", fmodel.getPropertyName());
	}
}
