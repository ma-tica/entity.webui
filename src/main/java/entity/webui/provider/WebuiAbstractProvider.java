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
import javax.faces.event.AjaxBehaviorListener;

import org.omnifaces.converter.SelectItemsConverter;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
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
	OutputLabel buidLabel(FieldModel fmodel)
	{
		OutputLabel label = new OutputLabel();
		
		/*
		 * Label can contains expression #{....}
		 */

		String caption = fmodel.getCaption().isEmpty() ? fmodel.getPropertyName() : fmodel.getCaption();
		label.setValueExpression("value", Utility.createExpression(caption, String.class));
		
		label.setFor(fmodel.getId());
		label.setId(fmodel.getId() + "_label");
		
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
		
		
		input.setValueExpression("disabled", Utility.createExpression(fmodel.getReadonlyExpression(), boolean.class));
		
		
		return input;
	}

	
	protected InputTextarea buildInputTextArea(FieldModel fmodel)
	{
		InputTextarea input = new InputTextarea();
		
		
		this.partialBuildeComponent(fmodel, input);		
		
		
		input.setStyle("width: 100%;");
		
		
		input.setValueExpression("disabled", Utility.createExpression(fmodel.getReadonlyExpression(), boolean.class));
		
		
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
		
		if (fmodel.getPropertyType().isEnum()) {
			UISelectItem item = new UISelectItem();
			item.setItemLabel("");
			item.setItemValue("");
			item.setNoSelectionOption(true);
			somenu.getChildren().add(item);

			for(int i=0; i < fmodel.getPropertyType().getEnumConstants().length; i++)
			{
				
				item = new UISelectItem();
				item.setItemLabel(fmodel.getPropertyType().getEnumConstants()[i].toString());
				item.setItemValue(fmodel.getPropertyType().getEnumConstants()[i]);
				somenu.getChildren().add(item);
			}
			
		}else if (fmodel.getPropertyType().getName().equals("java.lang.Boolean") || 
				fmodel.getPropertyType().getName().equals("boolean") ) {
			UISelectItem item;
			if (fmodel.getPropertyType().getName().equals("java.lang.Boolean")) {
				item = new UISelectItem();
				item.setItemLabel("");
				item.setItemValue("");
				item.setNoSelectionOption(true);
				somenu.getChildren().add(item);
			}

			item = new UISelectItem();
			item.setValueExpression("itemLabel", Utility.createExpression("Si", String.class));
			item.setItemValue(true);
			somenu.getChildren().add(item);

			item = new UISelectItem();
			item.setValueExpression("itemLabel", Utility.createExpression("No", String.class));
			item.setItemValue(false);
			somenu.getChildren().add(item);

		}else{
			UISelectItems items = new UISelectItems();
			String selectItemsExp;

			selectItemsExp = fmodel.getFillSelectionListExpression(); // String.format("#{%s.%s}", fmodel.getRelatedBeanControllerName(), "findAll()");
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
		}
		
		
		
		somenu.setStyle("width: 80%;");
			
		somenu.setValueExpression("disabled", Utility.createExpression(fmodel.getReadonlyExpression(), boolean.class));
		somenu.setValueExpression("readonly", Utility.createExpression(fmodel.getReadonlyExpression(), boolean.class));
				
		return somenu;
	}
	
	protected SelectBooleanCheckbox buildSelectBooleanCheckBox(FieldModel fmodel)
	{
		SelectBooleanCheckbox checkbox = new SelectBooleanCheckbox();
		
		this.partialBuildeComponent(fmodel, checkbox);
		
		return checkbox;	

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
		
		
		if (fmodel.getEvent() != null && !fmodel.getEvent().isEmpty())
		{
			input.addClientBehavior(fmodel.getEvent(), Utility.createAjaxBejhaviour(fmodel.getEventListenerExpression(), fmodel.getEventUpdateExpression()));
		}
		
		
		
		
	}
	
}
