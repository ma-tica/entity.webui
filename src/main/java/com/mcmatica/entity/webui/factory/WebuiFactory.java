package com.mcmatica.entity.webui.factory;

import java.util.List;

import javax.faces.component.html.HtmlPanelGroup;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.menubutton.MenuButton;
import org.primefaces.component.panel.Panel;

import com.mcmatica.entity.webui.model.scanner.FieldModel;

public interface WebuiFactory {

	HtmlPanelGroup buildPanelGrid();
	
	

	//List<FieldModel> buildShortListFields();

	List<FieldModel> getFields();

	DataTable buildSelectionGrid();

	Panel buildEditFormDialogs();
	
	MenuButton buildMenuFunctions();

}
