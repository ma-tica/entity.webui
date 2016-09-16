package entity.webui.provider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;

import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;

import entity.webui.model.FieldModel;

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


		for(FieldModel field : this.fields)
		{
			this.buildFieldController(field, panelBuilder);
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
	
	
	private void buildFieldController(FieldModel field, PanelBuilder builder) {
		
		UIComponent input = null;
		switch (field.getEditorComponent()) {
		case INPUT_TEXT:
		case INPUT_DATE:
			input = this.buildInputText(field);
			break;
		case SELECTION_ONE_MENU:
			input = this.buildSelectOneMenu(field);
			//input = this.buildInputText(field);
			break;
		default:
			break;
		}
		
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
