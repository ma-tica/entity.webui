package entity.webui.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;

import entity.webui.annotation.Webui;
import entity.webui.annotation.WebuiField;
import entity.webui.model.BaseEntityModel;
import entity.webui.model.FieldModel;

public class WebuiFactoryImpl<T extends BaseEntityModel> implements WebuiFactory {

	//private Class<T> clazz;
	
	private List<FieldModel> fields;
	
	private List<FieldModel> shortListFields;
	
	private String beanName;
	
	private Webui clazzAnnotation;
	
	
	private ResourceBundle labels; 
	
	/**
	 * Constructor
	 * 
	 * @param clazz
	 * @param beanName
	 */
	public WebuiFactoryImpl(Class<T> clazz, String managedBeanName)
	{
		
		this.clazzAnnotation = this.readClassAnnotation(clazz);
		this.beanName = managedBeanName;
		this.fillFieldList(clazz, beanName);
		
	}

	public WebuiFactoryImpl(Class<T> clazz)
	{
		
		this.clazzAnnotation = this.readClassAnnotation(clazz);
		this.beanName = this.clazzAnnotation.beanControllerName();
		this.fillFieldList(clazz, beanName);
		
	}


	
	public ResourceBundle getLabels() {
		if (this.labels == null)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			this.labels = context.getApplication().getResourceBundle(context, "mclbl");

		}
		return this.labels;
	}

	@Override
	public PanelGrid buildPanelGrid()
	{
		PanelGrid panel = new PanelGrid();
		
		/*
		 * Sort fields
		 */
		this.sortFieldByFormPosition();
		
		/*
		 * Instantiate the panel builder class
		 */		
		PanelBuilder panelBuilder = new PanelBuilder(panel, this.clazzAnnotation.panelColumns(), this.clazzAnnotation.title());
		
		for(FieldModel field : this.fields)
		{
			switch (field.getController()) {
			case INPUT_TEXT:
				this.buildInputText(field, panelBuilder);
				break;

			default:
				break;
			}
		}
		
		return panel;
	}
	
	
	@Override
	public List<FieldModel> buildShortListFields()
	{
		Collections.sort(this.shortListFields, new Comparator<FieldModel>() {

			@Override
			public int compare(FieldModel o1, FieldModel o2) {
				if (o1.getShortListPosition() == o2.getShortListPosition()) {
					return 0;
				} else if (o1.getShortListPosition() > o2.getShortListPosition()) {
					return 1;
				} else {
					return -1;
				}

			}
		});

		return this.shortListFields;
	}
	
	
	
/*
 * --------------------------
 * P R I V A T E	
 */
	private Webui readClassAnnotation(Class<T> clazz)
	{	
		return clazz.getAnnotation(Webui.class);
	}
	
	private void fillFieldList(Class<T> clazz, String beanControllerName)
	{
		this.fields = new ArrayList<FieldModel>();
		this.shortListFields = new ArrayList<FieldModel>();
		for (Field field : clazz.getDeclaredFields())
		{
			WebuiField webui = field.getAnnotation(WebuiField.class);
			if (webui != null)
			{
				FieldModel fmodel = new FieldModel();
				fmodel.setCaption(webui.caption());
				fmodel.setController(webui.controller());
				fmodel.setPropertyName(field.getName());
				fmodel.setBeanControllerName(beanControllerName);
				fmodel.setClazz(field.getType());
				fmodel.setRequired(webui.required());
				fmodel.setShortListPosition(webui.shortListPosition());
				fmodel.setColSpan(webui.colSpan());
				fmodel.setFormPosition(webui.formPosition());
				this.fields.add(fmodel);				
				if (fmodel.getShortListPosition() > 0)
				{
					this.shortListFields.add(fmodel);
				}
			}
		}
	}
	
	private void sortFieldByFormPosition()
	{
		Collections.sort(this.fields, new Comparator<FieldModel>() {

			@Override
			public int compare(FieldModel o1, FieldModel o2) {
				if (o1.getFormPosition() == o2.getFormPosition()) {
					return 0;
				} else if (o1.getFormPosition() > o2.getFormPosition()) {
					return 1;
				} else {
					return -1;
				}

			}
		});
	}
	
	private void buildInputText(FieldModel field, PanelBuilder builder)
	{
		/*
		 * Input text box
		 */
		InputText input = new InputText();				
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ValueExpression valueEx = app.getExpressionFactory().createValueExpression(context.getELContext(), this.elValue(field), field.getClazz());
		input.setValueExpression("value", valueEx);		
		input.setId(field.getId());
		input.setRequired(field.isRequired());
		
		if (field.getCaption().startsWith("#{")) {
			ValueExpression valueExCaption = app.getExpressionFactory().createValueExpression(context.getELContext(),field.getCaption(), field.getClazz());
			input.setRequiredMessage(valueExCaption.getValue(context.getELContext()) + " " + this.getLabels().getString("common.ismandatory"));
		}else{
			input.setRequiredMessage(field.getCaption() + " " + this.getLabels().getString("common.ismandatory"));
		}
		input.setStyle("width: 100%;");
					
		/*
		 * Label
		 */
		OutputLabel label = this.buidLabel(field);
		
		/*
		 * message validation
		 */
		Message message = this.buildMessage(field);
		
		/*
		 * input + message together in the same cell
		 */
		HtmlPanelGroup div = new HtmlPanelGroup();
		div.getChildren().add(input);
		div.getChildren().add(message);
		
		
		
		/*
		 * add to form panel 
		 */
		builder.addToPanel(label, 1);
		builder.addToPanel(div, field.getColSpan());
		//builder.addToPanel(message, 1);
		
	}
	
	
	private OutputLabel buidLabel(FieldModel field)
	{
		OutputLabel label = new OutputLabel();
		if (field.getCaption().startsWith("#{"))
		{
			FacesContext context = FacesContext.getCurrentInstance();
			Application app = context.getApplication();
			ValueExpression valueEx = app.getExpressionFactory().createValueExpression(context.getELContext(),field.getCaption(), field.getClazz());
			label.setValueExpression("value", valueEx);
			
			
		}else{
			label.setValue(field.getCaption());
		}
		label.setFor(field.getId());
		
		return label;
	}
	
	private Message buildMessage(FieldModel field)
	{
		Message message = new Message();
		message.setFor(field.getId());
		message.setDisplay("text");
		return message;
	}
	
	private String elValue(FieldModel fmodel)
	{
		String el = "#{%s.%s.%s}";
		return String.format(el, fmodel.getBeanControllerName(), "selected", fmodel.getPropertyName());
	}
}
