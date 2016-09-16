package entity.webui.provider;

import java.util.List;
import java.util.ResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;

import org.omnifaces.converter.SelectItemsConverter;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import entity.webui.common.Utility;
import entity.webui.model.FieldModel;

abstract class WebuiAbstractProvider {
	
	protected ResourceBundle labelProvider;
	
	/**
	 * Field label
	 * 
	 * @param field
	 * @return
	 */
	OutputLabel buidLabel(FieldModel field)
	{
		OutputLabel label = new OutputLabel();
		
		/*
		 * Label can contains expression #{....}
		 */
		if (field.getCaption().startsWith("#{"))
		{
			FacesContext context = FacesContext.getCurrentInstance();
			Application app = context.getApplication();
			ValueExpression valueEx = app.getExpressionFactory().createValueExpression(context.getELContext(),field.getCaption(), field.getPropertyType());
			label.setValueExpression("value", valueEx);
		}else{
			label.setValue(field.getCaption());
		}
		label.setFor(field.getId());
		
		return label;
	}

	protected String elValue(FieldModel fmodel)
	{		
		/*
		 * e.g. crud.selected.fieldname
		 */
		String el = "#{%s.%s.%s}";
		return String.format(el, fmodel.getBeanControllerName(), "selected", fmodel.getPropertyName());
	}

	protected Message buildMessage(FieldModel field)
	{
		Message message = new Message();
		message.setFor(field.getId());
		message.setDisplay("text");
		return message;
	}
	
	/**
	 * Input text box
	 * 
	 * @param field
	 * @return
	 */
	protected InputText buildInputText(FieldModel field)
	{
		InputText input = new InputText();
		
//		/*
//		 * create the value expression for Value attribute
//		 */
//		FacesContext context = FacesContext.getCurrentInstance();
//		Application app = context.getApplication();
//		ValueExpression valueEx = app.getExpressionFactory().createValueExpression(context.getELContext(), this.elValue(field), field.getClazz());
//		input.setValueExpression("value", valueEx);		
//		
//		
//		/*
//		 * Label can contains expression #{....}
//		 */
//		if (field.getCaption().startsWith("#{")) {
//			ValueExpression valueExCaption = app.getExpressionFactory().createValueExpression(context.getELContext(),field.getCaption(), field.getClazz());
//			input.setRequiredMessage(valueExCaption.getValue(context.getELContext()) + " " + labelProvider.getString("common.ismandatory"));
//		}else{
//			input.setRequiredMessage(field.getCaption() + " " + labelProvider.getString("common.ismandatory"));
//		}
		
		this.partialBuildeComponent(field, input);		
		
		
		input.setStyle("width: 100%;");
		input.setId(field.getId());
		input.setRequired(field.isRequired());
		
		return input;
	}

	protected SelectOneMenu buildSelectOneMenu(FieldModel field)
	{
		SelectOneMenu somenu = new SelectOneMenu();
		
		this.partialBuildeComponent(field, somenu);
		
//		String valueExp = String.format("#{%s.%s}", field.getBeanControllerName(),"selectedUser");
//		somenu.setValueExpression("value", this.createValueExp(valueExp, field.getClazz()));

		
		
		
		somenu.setConverter(new SelectItemsConverter());
		//somenu.setValueExpression("converter", this.createValueExp("omnifaces.SelectItemsConverter", String.class));
		
		
		UISelectItems items = new UISelectItems();
		String selectItemsExp = String.format("#{%s.%s}", field.getRelatedBeanControllerName(), "findAll()");
		items.setValueExpression("value", Utility.createValueExp(selectItemsExp, List.class));
		items.setValueExpression("var", Utility.createValueExp("itm", String.class));
		items.setValueExpression("itemLabel", Utility.createValueExp("#{itm.name}", String.class));
		items.setValueExpression("itemValue", Utility.createValueExp("#{itm}", field.getPropertyType()));	
		
		UISelectItem item = new UISelectItem();
		item.setItemLabel("");
		item.setItemValue("");
		item.setNoSelectionOption(true);
		
		
		
		somenu.getChildren().add(item);
		somenu.getChildren().add(items);
		
		
		
		somenu.setId(field.getId());
		somenu.setStyle("width: 80%;");
		somenu.setRequired(field.isRequired());
		
		
		
		return somenu;
	}
	
	private void partialBuildeComponent(FieldModel field, UIInput input)
	{
		
		/*
		 * create the value expression for Value attribute
		 */
//		FacesContext context = FacesContext.getCurrentInstance();
//		Application app = context.getApplication();		
//		ValueExpression valueEx = app.getExpressionFactory().createValueExpression(context.getELContext(), this.elValue(field), field.getClazz());
		input.setValueExpression("value", Utility.createValueExp(this.elValue(field), field.getPropertyType()));		
		
		
		/*
		 * Label can contains expression #{....}
		 */
		if (field.getCaption().startsWith("#{")) {
			ValueExpression valueExCaption = Utility.createValueExp(field.getCaption(), field.getPropertyType()) ;
			//		app.getExpressionFactory().createValueExpression(context.getELContext(),field.getCaption(), field.getClazz());
			//input.setRequiredMessage(valueExCaption.getValue(context.getELContext()) + " " + labelProvider.getString("common.ismandatory"));
			input.setRequiredMessage(valueExCaption.getValue(Utility.getELContext()) + " " + labelProvider.getString("common.ismandatory"));
		}else{
			input.setRequiredMessage(field.getCaption() + " " + labelProvider.getString("common.ismandatory"));
		}
		
	}
	
}
