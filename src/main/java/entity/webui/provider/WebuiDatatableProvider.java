package entity.webui.provider;

import java.util.List;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.component.UIInput;

import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.outputlabel.OutputLabel;

import entity.webui.common.Utility;
import entity.webui.model.BaseEntityModel;
import entity.webui.model.FieldModel;

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
		table.setId(childType.getName()+"_datatable");
		
		String var = this.fields.get(0).getBeanControllerName();
		table.setVar(this.fields.get(0).getBeanControllerName());

		for (FieldModel fmodel : this.fields) {
			Column column = new Column();
			OutputLabel header = this.buidLabel(fmodel);
			column.setHeader(header);
			column.getChildren().add(this.buildFieldController(fmodel));
			table.getChildren().add(column);
		}

		//Bottone -
		CommandButton remove = new CommandButton();
		remove.setValue("DEL");
		String deleteMethod = String.format("#{%s}", childType.getDeleteValueMethodName(var));
		MethodExpression actionListener = Utility.createMethodExp(deleteMethod, new Class[]{childType.getPropertyType()});
		remove.setActionExpression(actionListener);
		remove.setUpdate(table.getId());
		Column column = new Column();
		column.getChildren().add(remove);
		table.getChildren().add(column);

		
		return table;
	}

	private UIInput buildFieldController(FieldModel field) {

		UIInput input = null;
		switch (field.getEditorComponent()) {
		case INPUT_TEXT:
			input = this.buildInputText(field);
			break;
		case SELECTION_ONE_MENU:
			input = this.buildSelectOneMenu(field);
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
