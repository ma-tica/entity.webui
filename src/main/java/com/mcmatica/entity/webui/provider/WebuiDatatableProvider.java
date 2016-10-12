	package com.mcmatica.entity.webui.provider;

import java.util.List;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlOutputText;

import org.primefaces.component.celleditor.CellEditor;
import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.editor.Editor;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.roweditor.RowEditor;
import org.primefaces.component.tooltip.Tooltip;

import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.DetailListModel;
import com.mcmatica.entity.webui.model.FieldModel;

public class WebuiDatatableProvider<T extends BaseEntityModel> extends WebuiAbstractProvider {

	private List<FieldModel> fields;	
	private String listValueGetterSetter;
	private DetailListModel detailListModel;

	
	
	public WebuiDatatableProvider(List<FieldModel> fields, ResourceBundle labelProvider, String listValueGetterSetter, DetailListModel detailListModel) {
		this.fields = fields;
		this.labelProvider = labelProvider;
		this.listValueGetterSetter = listValueGetterSetter;
		this.detailListModel = detailListModel;
	}


	
	public DataTable buildDataTable() {
		DataTable table = new DataTable();
		

		table.setValueExpression("value", Utility.createExpression(this.listValueGetterSetter, List.class));

		String var = detailListModel.getPropertyName() ; //this.fields.get(0).getBeanControllerName();
		table.setVar(var);
		table.setId(detailListModel.getPropertyName());

		table.setSelectionMode("single");
		table.setValueExpression("rowKey", Utility.createExpression("#{" + var + ".id}", String.class));
		table.setEditable(true);
		table.setEditMode("cell");
//		table.setLiveResize(true);
//		table.setNativeElements(true);
//		String expr = String.format("#{%s.%s}", this.fields.get(0).getBeanControllerName(), "selected" );
//		table.setValueExpression("selection", Utility.createExpression(expr,  this.childType.getPropertyType()));

		String expr = String.format("#{%s}", detailListModel.getSelection());
		table.setValueExpression("selection", Utility.createExpression(expr,  this.detailListModel.getPropertyType()));
		
		
		
		//expr = String.format("#{%s.%s.%s.%s}", this.childType.getParentBeanName(), "selected" ,this.fields.get(0).getBeanControllerName() , "selected" );
		//table.addClientBehavior("rowSelect", Utility.createAjaxBejhaviour(expr , ":form_main"));
		
		
		if (this.detailListModel.getEvent() != null && !this.detailListModel.getEvent().getEventName().isEmpty()) {
			table.addClientBehavior(this.detailListModel.getEvent().getEventName(), 
									Utility.createAjaxBehaviour(this.detailListModel.getEvent().getEventListenerExpression(), 
									this.detailListModel.getEvent().getEventUpdateExpression()));
		}
		
		//if (fmodel.getEvent() != null && !fmodel.getEvent().isEmpty())
		//{
		//	input.addClientBehavior(fmodel.getEvent(), Utility.createAjaxBejhaviour(fmodel.getEventListenerExpression(), fmodel.getEventUpdateExpression()));
		//}
		
		
		/*
		 * Radio button per selezione riga
		 */

		Column selectionColumn = new Column();
//		selectionColumn.setSelectionMode("single");
		selectionColumn.setStyle("width:10px; text-align:center");
		table.getChildren().add(selectionColumn);
		
		
		for (FieldModel fmodel : this.fields) {
			UIComponent input = null;
			input = this.buildFieldController(fmodel);
			if (input.isRendered()) {
//				Column column = new Column();
//				OutputLabel header = this.buidLabel(fmodel);
//				column.setHeader(header);
//				column.getChildren().add(this.buildFieldController(fmodel));
//				table.getChildren().add(column);
				
				Column column = new Column();
				HtmlOutputText header = new HtmlOutputText();
				String caption = fmodel.getCaption().isEmpty() ? fmodel.getPropertyName() : fmodel.getCaption();
				header.setValueExpression("value", Utility.createExpression(caption, String.class));			
				column.setHeader(header);
				
				
				CellEditor cell = new CellEditor();
				
				HtmlOutputText outputcell = new HtmlOutputText();			
				String cellvalue = String.format("#{%s.%s}", table.getVar(), fmodel.getPropertyName());
				outputcell.setValueExpression("value", Utility.createExpression(cellvalue, fmodel.getPropertyType()));	
				cell.getFacets().put("output", outputcell);
				
				input.setValueExpression("value", Utility.createExpression(cellvalue, fmodel.getPropertyType()));
				cell.getFacets().put("input", input);
				
				column.getChildren().add(cell);
				
				table.getChildren().add(column);

			}
		}

		/*
		 * Row editor
		 */
		
//		Column editorColumn = new Column();
//		editorColumn.setStyle("width:32px");
//		RowEditor rowEdior = new RowEditor();
//		editorColumn.getChildren().add(rowEdior);
//		table.getChildren().add(editorColumn);
		
		table.setId(detailListModel.getPropertyName() + "_datatable");

		/*
		 *  Bottone -
		 */
		CommandButton remove = new CommandButton();
		
		remove.setIcon("fa fa-fw fa-remove");
		String deleteMethod = String.format("#{%s}", detailListModel.getDeleteValueMethodName(var));
		MethodExpression actionListener = Utility.createMethodExp(deleteMethod,
				new Class[] { detailListModel.getPropertyType() });
		remove.setActionExpression(actionListener);
		remove.setUpdate(table.getId());
		
		/*
		 * Tootltip Button
		 */
//		Tooltip minustooltip = new Tooltip();
//		minustooltip.setFor(remove.getId());  //ID bottone non definibile
//		minustooltip.setValue("Delete " + childType.getName()) ;

		Column column = new Column();
		column.setStyle("width:32px");
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
