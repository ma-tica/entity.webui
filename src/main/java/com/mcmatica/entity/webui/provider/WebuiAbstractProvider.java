package com.mcmatica.entity.webui.provider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.mcmatica.entity.webui.common.Constant;
import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.converter.AutocompleteConverter;
import com.mcmatica.entity.webui.converter.SelectItemsConverter;
import com.mcmatica.entity.webui.model.FieldModel;

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

		String caption = fmodel.getCaption();
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
		String el;
		if (fmodel.getLinkedParentField() != null && !fmodel.getLinkedParentField().isEmpty())
		{
			el = String.format("#{%s.%s.%s.%s}", fmodel.getBeanControllerName(), "selected", fmodel.getLinkedParentField(), fmodel.getLinkedValueExpression());
		}else{
			el = String.format("#{%s.%s.%s}", fmodel.getBeanControllerName(), "selected", fmodel.getPropertyName()); 
		}
		return el;
	}

	protected String elDbRefValue(FieldModel fmodel)
	{		
		/*
		 * e.g. crud.selected.fieldname
		 */
		String el = "#{%s.%s.%s." + Constant.PROPERTY_SELECTION_LABEL + "}";
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
		

		if (fmodel.getWidth() != null && !fmodel.getWidth().isEmpty())
		{
			input.setStyle("width: " + fmodel.getWidth() + ";");
		}else{
		
			input.setStyle("width: 100%;");
		}
		
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
			items.setValueExpression("itemLabel", Utility.createExpression("#{itm." + Constant.PROPERTY_SELECTION_LABEL + "}", String.class));
			items.setValueExpression("itemValue", Utility.createExpression("#{itm}", fmodel.getPropertyType()));	
			
			UISelectItem item = new UISelectItem();
			item.setItemLabel("");
			item.setItemValue("");
			item.setNoSelectionOption(true);
			somenu.getChildren().add(item);
			somenu.getChildren().add(items);
			//somenu.setEditable(true);
			somenu.setFilter(true);
			
		}
		
		

		if (fmodel.getWidth() != null && !fmodel.getWidth().isEmpty())
		{
			somenu.setStyle("width: " + fmodel.getWidth() + ";");
		}else{
		
			somenu.setStyle("width: calc(100% - 35px); ");
		}

		
		somenu.setValueExpression("autoWidth", Utility.createExpression("false", boolean.class));
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
	
	
	protected AutoComplete buildAutocomplete(FieldModel fmodel) {
		
		
		AutoComplete auto = new AutoComplete();
		
		this.partialBuildeComponent(fmodel, auto);
		
		auto.setConverter(new AutocompleteConverter());	

		
		String selectItemsExp = fmodel.getFillSelectionListExpression();
		auto.setCompleteMethod(Utility.createMethodExp(selectItemsExp, new Class[]{String.class}));
		
		auto.setValueExpression("var", Utility.createExpression("itm", String.class));
		auto.setValueExpression("itemLabel", Utility.createExpression("#{itm." + Constant.PROPERTY_SELECTION_LABEL + "}", String.class));
		auto.setValueExpression("itemValue", Utility.createExpression("#{itm}", fmodel.getPropertyType()));	
//		auto.setDropdown(true);
//		auto.setDropdownMode("current");
//		auto.setForceSelection(true);
		auto.setMaxResults(20);
		auto.setQueryDelay(500);
		
		auto.setCache(true);
		
		
		
		auto.setValueExpression("disabled", Utility.createExpression(fmodel.getReadonlyExpression(), boolean.class));
		auto.setValueExpression("readonly", Utility.createExpression(fmodel.getReadonlyExpression(), boolean.class));

		if (fmodel.getWidth() != null && !fmodel.getWidth().isEmpty())
		{
			auto.setStyle("width: " + fmodel.getWidth() + ";");
		}else{
		
			auto.setStyle("width: 100%");
		}

		auto.setSize(50);
		
		//auto.addClientBehavior("query", Utility.createClientBehaviour("$('.detail_tabview_jobber_gif').show(); ", "$('.detail_tabview_jobber_gif').hide();", null));
		
				
		
		
		return auto;
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
		
		input.setValueExpression("widgetVar", Utility.createExpression(fmodel.getId(), String.class) );
		input.setId(fmodel.getId());
		input.setValueExpression("required", Utility.createExpression(fmodel.getRequiredExpression(), boolean.class));
		input.setValueExpression("rendered", Utility.createExpression(fmodel.getVisibleExpression(), boolean.class));
		
		/*
		 * Add custom event
		 */
		if (fmodel.getEvent() != null && !fmodel.getEvent().getEventName().isEmpty())
		{
			input.addClientBehavior(fmodel.getEvent().getEventName(), Utility.createAjaxBehaviour(fmodel.getEvent().getEventListenerExpression(), fmodel.getEvent().getEventUpdateExpression()));
		}
		
		/*
		 * Add the default onChangeEnvent managed by the BaseWebuiBean class
		 */
//		if (fmodel.getEvent() == null ||  !fmodel.getEvent().getEventName().equals("change")) {
			input.addClientBehavior("change", Utility.createAjaxBehaviour(fmodel.getChangeEventExpression(), null));
//		}

	}
	
}
