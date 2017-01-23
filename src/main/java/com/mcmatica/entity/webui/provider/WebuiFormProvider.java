package com.mcmatica.entity.webui.provider;

import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.html.HtmlForm;

import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.panelgrid.PanelGrid;

import com.mcmatica.entity.webui.annotation.MCWebui;
import com.mcmatica.entity.webui.model.DetailListModel;
import com.mcmatica.entity.webui.model.FieldModel;

public class WebuiFormProvider extends WebuiPanelProvider {
	
	private List<FieldModel> fields;	
	private String listValueGetterSetter;
	private DetailListModel detailListModel;
	private MCWebui classAnnotation; 


	public WebuiFormProvider(List<FieldModel> fields, MCWebui classAnnotation, 
							 ResourceBundle labelProvider, String listValueGetterSetter, 
							 DetailListModel detailListModel) 
	{
		super(fields, 
			 classAnnotation.panelColumns(), classAnnotation.columnClasses(), classAnnotation.title(), 
			 detailListModel.getParentPropertyName(),
			 labelProvider);
		this.fields = fields;
//		this.labelProvider = labelProvider;
		this.listValueGetterSetter = listValueGetterSetter;
		this.detailListModel = detailListModel;
	
	}


	
	
	public Dialog buildForm()
	{

		
		
		Dialog dialog = new Dialog();
		dialog.setId(detailListModel.getPropertyName() + "_form");
		dialog.setWidgetVar(dialog.getId());
		dialog.setModal(true);
		PanelGrid panel = this.buildPanelGrid();
		dialog.getChildren().add(panel);
				
		return dialog;
	}
	
	
}
