package entity.webui.factory;

import javax.faces.component.UIComponent;

import org.primefaces.component.column.Column;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;





import org.primefaces.component.row.Row;

class PanelBuilder {

	private int childCount = 0;
	private Row row;
	private int numColumns;
	private PanelGrid panel;
	
	public PanelBuilder(PanelGrid panel, int numColumns, String header)
	{
		this.panel = panel;
		this.numColumns = numColumns;
		
		if (header != null & !header.isEmpty() )
		{
			this.addHeader(header);
		}
	}
	
	public void addToPanel(UIComponent component, int colspan)
	{
		
		if (childCount == 0 || childCount >= this.numColumns )
		{
			/*
			 * add a row
			 */
//			if (this.row != null)
//			{
//				this.panel.getChildren().add(this.row);
//				
//			}					
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
