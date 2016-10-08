package com.mcmatica.entity.webui.factory;

import java.util.List;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;

import com.mcmatica.entity.webui.model.FieldModel;

public interface WebuiFactory {

	Panel buildPanelGrid();
	
	

	//List<FieldModel> buildShortListFields();

	List<FieldModel> getFields();

	DataTable buildSelectionGrid();

}
