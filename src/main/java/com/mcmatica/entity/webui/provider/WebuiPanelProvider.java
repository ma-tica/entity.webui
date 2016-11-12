package com.mcmatica.entity.webui.provider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlPanelGroup;

import org.primefaces.component.column.Column;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.row.Row;

import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.FieldModel;

public class WebuiPanelProvider extends WebuiAbstractProvider {
	

	private List<FieldModel> fields;
	private int columnNum;
	private String title;
	private String parentField;
	

	/**
	 * Constructor 
	 * 
	 * @param fields
	 * @param columnNum
	 * @param title
	 * @param parentField
	 * 				reference to parent field. Needed for child panels
	 * @param title
	 */
	public WebuiPanelProvider(List<FieldModel> fields, int columnNum, String title, String parentField, ResourceBundle labelProvider) {
		super();
		this.fields = fields;
		this.columnNum = columnNum;
		this.title = title;
		this.parentField = parentField;
		this.labelProvider = labelProvider;
	}
	
	/**
	 * Compose the row an fields into a primeface PanelGrid
	 * 
	 * @author matteo
	 *
	 */
	class PanelBuilder {

		private int childCount = 0;
		private Row row;
		private int numColumns;
		private PanelGrid panel;
		
		public PanelBuilder(PanelGrid panel, int numColumns, String header)
		{
			this.panel = panel;
			this.numColumns = numColumns;
			
			if (header != null && !header.isEmpty() )
			{				
				this.addHeader(Utility.createValue(header, String.class));
				//this.addHeader(Utility.createExpression(header, String.class));
			}
		}
		
		public void addToPanel(UIComponent component, int colspan)
		{
			
			if (childCount == 0 || childCount >= this.numColumns )
			{
				/*
				 * add a row
				 */
				this.childCount = 0;
				this.row = new Row();
				this.panel.getChildren().add(row);
			}
				
			Column col = new Column();
			col.setColspan(colspan);	
			
			col.getChildren().add(component);
			this.row.getChildren().add(col);
			this.childCount += colspan;
		}
		
		
		private void addHeader(String header)
		{
			Row rowHeader = new Row();
			Column colHeader = new Column();
			colHeader.setColspan(this.numColumns);
			OutputLabel lbl = new OutputLabel();
			lbl.setValue(header);
			colHeader.getChildren().add(lbl);
			rowHeader.getChildren().add(colHeader);
			
			this.panel.getFacets().put("header", rowHeader);
			
		}
	}
	
	
	
	public PanelGrid buildPanelGrid()
	{
		/*
		 * Sort fields
		 */
		this.sortFieldByFormPosition();
		
		PanelGrid panel = new PanelGrid();
		
		/*
		 * Instantiate the panel builder class and creates the main header panel
		 */		
		PanelBuilder panelBuilder = new PanelBuilder(panel, this.columnNum, this.title);


		for(FieldModel fmodel : this.fields)
		{
			this.buildFieldController(fmodel, panelBuilder);
		}

		
		return panel;
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
	
	
	private void buildFieldController(FieldModel fmodel, PanelBuilder builder) {
		
		UIComponent input = null;
		switch (fmodel.getEditorComponent()) {
		case INPUT_TEXT:		
			input = this.buildInputText(fmodel);
			break;
		case INPUT_TEXTAREA:		
			input = this.buildInputTextArea(fmodel);
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
		case BOOLEAN_CHECKBOX:
			input = this.buildSelectBooleanCheckBox(fmodel);
			break;
		default:
			break;
		}
		
		
		if (!input.isRendered() )
		{
			return ;
		}
		
		/*
		 * Label
		 */
		OutputLabel label = this.buidLabel(fmodel);
		
		
		/*
		 * message validation
		 */
		Message message = null;
		switch (fmodel.getEditorComponent()) {
		case INPUT_TEXT:
		case INPUT_TEXTAREA:			
		case INPUT_DATE:
		case INPUT_DATETIME:
		case SELECTION_ONE_MENU:
		case BOOLEAN_CHECKBOX:
			message = this.buildMessage(fmodel);
			break;
		default:
			break;
		}
		
		/*
		 * input + message together in the same cell
		 */
		HtmlPanelGroup div = null;
		if (message != null) {
			div = new HtmlPanelGroup();
			div.getChildren().add(input);
			div.getChildren().add(message);
		}else{
			
		}
		
		
		/*
		 * add to form panel 
		 */
		builder.addToPanel(label, 1);
		if (div != null) {
			builder.addToPanel(div, fmodel.getColSpan());
		}else{
			builder.addToPanel(input, fmodel.getColSpan());
		}
		
		/*
		 * store the input editor controller into the fields list
		 * for future manipulation after creation
		 */
		//fmodel.setInputEditorController((UIInput) input);
		
	}
	
	

	@Override
	protected String elValue(FieldModel fmodel)
	{
		
		if (this.parentField != null)
		{
			/*
			 * e.g. crud.selected.parenfField.fieldname
			 */
			String el = "#{%s.%s.%s}";
			return String.format(el, this.parentField, "selected", fmodel.getPropertyName());
		}else {
			/*
			 * e.g. crud.selected.fieldname
			 */
			return super.elValue(fmodel);
		}
	}

}
