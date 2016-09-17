package entity.webui.provider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;

import org.omnifaces.converter.SelectItemsConverter;
import org.primefaces.component.calendar.Calendar;
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
//		if (field.getCaption().startsWith("#{"))
//		{
//			FacesContext context = FacesContext.getCurrentInstance();
//			Application app = context.getApplication();
//			ValueExpression valueEx = app.getExpressionFactory().createValueExpression(context.getELContext(),field.getCaption(), String.class);
//			label.setValueExpression("value", valueEx);
//		}else{
//			label.setValue(field.getCaption());
//		}
//		
		label.setValueExpression("value", Utility.createExpression(field.getCaption(), String.class));
		
		label.setFor(field.getId());
		label.setId(field.getId() + "_label");
		
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

	protected Message buildMessage(FieldModel fmodel)
	{
		Message message = new Message();
		message.setFor(fmodel.getId());
		message.setDisplay("text");
		message.setId(fmodel.getId()+"_message");
		
		return message;
	}
	
	/**
	 * Input text box
	 * 
	 * @param field
	 * @return
	 */
	protected InputText buildInputText(FieldModel fmodel)
	{
		InputText input = new InputText();
		
		
		this.partialBuildeComponent(fmodel, input);		
		
		
		input.setStyle("width: 100%;");
		
		
		input.setValueExpression("readonly", Utility.createExpression(fmodel.getReadonlyExpression(), boolean.class));
		
		return input;
	}

	protected Calendar buildInputCalendar(FieldModel fmodel, boolean time)
	{
		Calendar calendar = new Calendar();
		
		this.partialBuildeComponent(fmodel, calendar);
		
		
		
		
		
		
		if (time)
		{
			Locale locale = FacesContext.getCurrentInstance().getELContext().getLocale();
			SimpleDateFormat f = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
			calendar.setPattern(f.toPattern());
		}
		
		calendar.setValueExpression("disabled", Utility.createExpression(fmodel.getReadonlyExpression(), boolean.class));
		calendar.setMode("popup");
		calendar.setShowOn("button");
		
		return calendar;
	}
	
	/**
	 * Select One Menu
	 * @param field
	 * @return
	 */
	protected SelectOneMenu buildSelectOneMenu(FieldModel fmodel)
	{
		SelectOneMenu somenu = new SelectOneMenu();
		
		this.partialBuildeComponent(fmodel, somenu);
		
		somenu.setConverter(new SelectItemsConverter());	
		
		UISelectItems items = new UISelectItems();
		String selectItemsExp = String.format("#{%s.%s}", fmodel.getRelatedBeanControllerName(), "findAll()");
		items.setValueExpression("value", Utility.createExpression(selectItemsExp, List.class));
		items.setValueExpression("var", Utility.createExpression("itm", String.class));
		items.setValueExpression("itemLabel", Utility.createExpression("#{itm.selectionLabel}", String.class));
		items.setValueExpression("itemValue", Utility.createExpression("#{itm}", fmodel.getPropertyType()));	
		
		UISelectItem item = new UISelectItem();
		item.setItemLabel("");
		item.setItemValue("");
		item.setNoSelectionOption(true);
		
		
		
		somenu.getChildren().add(item);
		somenu.getChildren().add(items);
		
		
		
		
		somenu.setStyle("width: 80%;");
		
		
		somenu.setValueExpression("readonly", Utility.createExpression(fmodel.getReadonlyExpression(), boolean.class));
		
		
		return somenu;
	}
	
	private void partialBuildeComponent(FieldModel fmodel, UIInput input)
	{
		
		/*
		 * create the value expression for Value attribute
		 */
		input.setValueExpression("value", Utility.createExpression(this.elValue(fmodel), fmodel.getPropertyType()));		
		
		
		/*
		 * Label can contains expression #{....}
		 */
		
		//input.setRequiredMessage(Utility.createValue(stringExpression, type)(fmodel.getCaption(), String.class) + " " + labelProvider.getString("common.ismandatory"));
		input.setValueExpression("requiredMessage", Utility.createExpression(fmodel.getCaption()+ " " + labelProvider.getString("common.ismandatory"), String.class) );
		
		
		input.setId(fmodel.getId());
		input.setValueExpression("required", Utility.createExpression(fmodel.getRequiredExpression(), boolean.class));
		input.setValueExpression("rendered", Utility.createExpression(fmodel.getVisibleExpression(), boolean.class));
		
		
		
		
		
	}
	
}
