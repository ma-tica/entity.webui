package com.mcmatica.entity.webui.factory;

import java.util.List;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.panel.Panel;

import com.mcmatica.entity.webui.bean.BaseUi;
import com.mcmatica.entity.webui.model.DetailListModel;
import com.mcmatica.entity.webui.model.FieldModel;

public interface WebuiFactory {

	Panel buildPanelGrid();
	
	

	//List<FieldModel> buildShortListFields();

	List<FieldModel> getFields();

	DataTable buildSelectionGrid();


	Panel buildEditFormDialogs();

}
