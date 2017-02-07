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
import org.primefaces.component.contextmenu.ContextMenu;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.menubutton.MenuButton;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.roweditor.RowEditor;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DynamicMenuModel;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;

import com.mcmatica.entity.webui.common.Constant;
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
		
		/* value */
		table.setValueExpression("value", Utility.createExpression(this.listValueGetterSetter, List.class));

		/* var */
		String var = "item"; //detailListModel.getPropertyName() ; //this.fields.get(0).getBeanControllerName();
		table.setVar(var);
		
		/* Id */
		//table.setId(detailListModel.getPropertyName());
		table.setId(detailListModel.getPropertyName() + "_datatable");

		/* selection mode */
		table.setSelectionMode("single");
		table.setValueExpression("rowKey", Utility.createExpression("#{" + var + ".id}", String.class));
		String expr = String.format("#{%s}", detailListModel.getSelection());
		table.setValueExpression("selection", Utility.createExpression(expr,  this.detailListModel.getPropertyType()));		

		/* editable */
		table.setEditable(false);
		//table.setEditMode("cell");
		//table.setEditingRow(true);

		table.setResizableColumns(true);
		
		/* table style */
		//table.setValueExpression("tableStyle", Utility.createExpression("width: auto", String.class));

		
		
		if (this.detailListModel.getEvent() != null && !this.detailListModel.getEvent().getEventName().isEmpty()) {
			table.addClientBehavior(this.detailListModel.getEvent().getEventName(), 
									Utility.createAjaxBehaviour(this.detailListModel.getEvent().getEventListenerExpression(), 
									this.detailListModel.getEvent().getEventUpdateExpression()));
		}

		/* rowSelect event */
		table.addClientBehavior("rowSelect", 
				Utility.createAjaxBehaviour(null, 
						"form_main:detail_tabview:" + this.detailListModel.getPropertyName() + "_remove_button, form_main:detail_tabview:" + this.detailListModel.getPropertyName() + "_edit_button" ));
		
		
		
		
		
		/*
		 * Column per selezione riga
		 */
//		Column selectionColumn = new Column();
//		selectionColumn.setStyle("width:32px;");
//		table.getChildren().add(selectionColumn);
		
		
		
		
		
		for (FieldModel fmodel : this.fields) {
			UIComponent input = null;
			input = this.buildFieldController(fmodel);
			if (input.isRendered()) {
				
				/* column header */
				Column column = new Column();
				HtmlOutputText header = new HtmlOutputText();
				String caption = fmodel.getCaption().isEmpty() ? fmodel.getPropertyName() : fmodel.getCaption();
				header.setValueExpression("value", Utility.createExpression(caption, String.class));			
				column.setHeader(header);
				
				/* cell editor */
//				CellEditor cell = new CellEditor();				
				HtmlOutputText outputcell = new HtmlOutputText();
		
				outputcell.setStyle("display: block;");
				
				String valueexpr;
				if (fmodel.getFillSelectionListExpression() != null && !fmodel.getFillSelectionListExpression().isEmpty()) {
					
					//valueexpr=  String.format("#{%s.%s.%s}", table.getVar(), fmodel.getPropertyName(), Constant.PROPERTY_SELECTION_LABEL);
					valueexpr=  this.selectionLabelString(table.getVar() + "." + fmodel.getPropertyName(), fmodel);
					outputcell.setValueExpression("value", Utility.createExpression(valueexpr, String.class));
					
				}else{
					valueexpr = String.format("#{%s.%s}", table.getVar(), fmodel.getPropertyName());
					if (fmodel.getLinkedParentField() != null && !fmodel.getLinkedParentField().isEmpty())
					{
						valueexpr = String.format("#{%s.%s.%s}", table.getVar(), fmodel.getLinkedParentField(), fmodel.getLinkedValueExpression());
					}
					
					outputcell.setValueExpression("value", Utility.createExpression(valueexpr, fmodel.getPropertyType()));
					
				}
				
//				valueexpr = String.format("#{%s.%s}", table.getVar(), fmodel.getPropertyName());
//				if (fmodel.getLinkedParentField() != null && !fmodel.getLinkedParentField().isEmpty())
//				{
//					valueexpr = String.format("#{%s.%s.%s}", table.getVar(), fmodel.getLinkedParentField(), fmodel.getLinkedValueExpression());
//				}
//				input.setValueExpression("value", Utility.createExpression(valueexpr, fmodel.getPropertyType()));
//				cell.getFacets().put("input", input);
				
				//outputcell.setValueExpression("readonly", Utility.createExpression("true", Boolean.class));
				//outputcell.setId(outputcell.getId()+"_readonly");
				
//				cell.getFacets().put("output", outputcell);					
//				cell.getFacets().put("input", input);
				
				if (fmodel.getWidth() != null && !fmodel.getWidth().isEmpty()) {
					column.setWidth(fmodel.getWidth());
				}
				
				/* add cell editor to the colum */
//				column.getChildren().add(cell);
	
				
				column.getChildren().add(outputcell);
				
				
				/* add column to table */
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
		
		

		/*
		 *  Bottone -
		 */
//		CommandButton remove = new CommandButton();
//		
//		remove.setIcon("fa fa-fw fa-remove");
//		String deleteMethod = String.format("#{%s}", detailListModel.getDeleteValueMethodName(var));
//		MethodExpression actionListener = Utility.createMethodExp(deleteMethod,
//				new Class[] { detailListModel.getPropertyType() });
//		remove.setActionExpression(actionListener);
//		remove.setUpdate(table.getId());
		
//		MethodExpression innerActionListener = Utility.createMethodExp("onRemoveChild",
//				new Class[] { detailListModel.getPropertyType() });
		
		
// Non serve		remove.addClientBehavior("click", Utility.createAjaxBehaviour(detailListModel.getChangeEventExpression(), null));
		
		
		
		/*
		 * Tootltip Button
		 */
//		Tooltip minustooltip = new Tooltip();
//		minustooltip.setFor(remove.getId());  //ID bottone non definibile
//		minustooltip.setValue("Delete " + childType.getName()) ;

//		Column column = new Column();
//		column.setStyle("width:32px");
//		column.getChildren().add(remove);
//		
//		
//		table.getChildren().add(column);

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
			input = this.buildSelectOneMenu(fmodel);
			break;
		case AUTOCOMPLETE:
			input = this.buildAutocomplete(fmodel);
			break;
		case SEARCH:
			input = this.buildInputText(fmodel);
			break;
		default:
			break;
		}
		return input;
	}

//	private HtmlOutputText buildOutputFieldController(FieldModel fmodel, String valueExpression) {
//
//		HtmlOutputText output = null;
//		switch (fmodel.getEditorComponent()) {
//		case INPUT_TEXT:			
//			output.setValueExpression("value", Utility.createExpression(valueExpression, fmodel.getPropertyType()));	
//			break;
//		case INPUT_DATE:
//			input = this.buildInputCalendar(fmodel, false);
//			break;
//		case INPUT_DATETIME:
//			input = this.buildInputCalendar(fmodel, true);
//			break;
//		case SELECTION_ONE_MENU:
//			input = this.buildSelectOneMenu(fmodel);
//			
//			output.setValueExpression("value", Utility.createExpression("#{itm.selectionLabel}", String.class));
//		default:
//			break;
//		}
//		return output;
//	}

	
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
