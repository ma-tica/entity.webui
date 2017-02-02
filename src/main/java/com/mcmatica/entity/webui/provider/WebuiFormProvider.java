package com.mcmatica.entity.webui.provider;

import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.html.HtmlForm;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.panelgrid.PanelGrid;

import com.mcmatica.entity.webui.annotation.MCWebui;
import com.mcmatica.entity.webui.common.Utility;
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


	
	
	@Override
	protected String elValue(FieldModel fmodel) {
		// TODO Auto-generated method stub
//		String el = "#{%s.%s.%s.%s}";
//		return String.format(el, fmodel.getBeanControllerName(), "selected", fmodel.getLinkedParentField(), fmodel.getPropertyName());
		if (fmodel.getLinkedParentField() != null && !fmodel.getLinkedParentField().isEmpty())
		{
			return String.format("#{%s.%s.%s}", detailListModel.getSelection(), fmodel.getLinkedParentField(), fmodel.getLinkedValueExpression());
		}else{
			return String.format("#{%s.%s}", detailListModel.getSelection(),  fmodel.getPropertyName());			
		}

	}




	public HtmlForm buildForm()
	{

		
		
		Dialog dialog = new Dialog();
		dialog.setId(detailListModel.getPropertyName() + "_dlg");
		dialog.setWidgetVar(dialog.getId());
		dialog.setModal(true);
		PanelGrid panel = this.buildPanelGrid();
		dialog.getChildren().add(panel);
		//dialog.setClosable(false);
		
		// Commands button
		CommandButton buttonok = new CommandButton();
		buttonok.setType("submit");
		buttonok.setUpdate(":form_toolbar, :form_main:detail_tabview:" + detailListModel.getPropertyName() + "_datatable");
		buttonok.setIcon("fa fa-check");
		buttonok.setValueExpression("value", Utility.createExpression("#{mclbl['common.ok']}",String.class));
		buttonok.setOncomplete("PF('" + dialog.getId() +"').hide()");
		
		dialog.getChildren().add(buttonok);
		
		
		HtmlForm form = new HtmlForm();
		form.getChildren().add(dialog);
		
		form.setId(detailListModel.getPropertyName() + "_form");
		
		return form;
	}
	
	
}
