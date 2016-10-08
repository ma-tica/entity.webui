	package com.mcmatica.entity.webui.provider;

import java.util.List;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.outputlabel.OutputLabel;

import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.FieldModel;

public class WebuiDatatableProvider<T extends BaseEntityModel> extends WebuiAbstractProvider {

	private List<FieldModel> fields;	
	private String listValueGetterSetter;
	private ChildType<?> childType;

	
	
	public WebuiDatatableProvider(List<FieldModel> fields, ResourceBundle labelProvider, String listValueGetterSetter, ChildType<?> childType) {
		this.fields = fields;
		this.labelProvider = labelProvider;
		this.listValueGetterSetter = listValueGetterSetter;
		this.childType=childType;
	}


	
	public DataTable buildDataTable() {
		DataTable table = new DataTable();
		table.setValueExpression("value", Utility.createExpression(this.listValueGetterSetter, List.class));

		String var = this.fields.get(0).getBeanControllerName();
		table.setVar(this.fields.get(0).getBeanControllerName());

		for (FieldModel fmodel : this.fields) {
			UIComponent input = null;
			input = this.buildFieldController(fmodel);
			if (input.isRendered()) {
				Column column = new Column();
				OutputLabel header = this.buidLabel(fmodel);
				column.setHeader(header);
				column.getChildren().add(this.buildFieldController(fmodel));
				table.getChildren().add(column);
			}
		}

		table.setId(childType.getName() + "_datatable");

		// Bottone -
		CommandButton remove = new CommandButton();
		// remove.setValue("remove");
		remove.setIcon("fa fa-fw fa-remove");
		String deleteMethod = String.format("#{%s}", childType.getDeleteValueMethodName(var));
		MethodExpression actionListener = Utility.createMethodExp(deleteMethod,
				new Class[] { childType.getPropertyType() });
		remove.setActionExpression(actionListener);
		remove.setUpdate(table.getId());
		Column column = new Column();
		column.getChildren().add(remove);
		table.getChildren().add(column);

		return table;
	}

	private UIInput buildFieldController(FieldModel fmodel) {

		UIInput input = null;
		switch (fmodel.getEditorComponent()) {
		case INPUT_TEXT:
			input = this.buildInputText(fmodel);
			break;
		case INPUT_DATE:
			input = this.buildInputCalendar(fmodel, false);
			break;
		case INPUT_DATETIME:
			input = this.buildInputCalendar(fmodel, true);
			break;
		case SELECTION_ONE_MENU:
			break;
		default:
			break;
		}
		return input;
	}

	@Override
	protected String elValue(FieldModel fmodel)
	{		
		/*
		 * e.g. crud.selected.fieldname
		 */
		String el = "#{%s.%s}";
		return String.format(el, fmodel.getBeanControllerName(), fmodel.getPropertyName());
	}


}
