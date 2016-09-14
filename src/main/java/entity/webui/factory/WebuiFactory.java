package entity.webui.factory;

import java.util.List;

import org.primefaces.component.panel.Panel;

import entity.webui.model.FieldModel;

public interface WebuiFactory {

	Panel buildPanelGrid();

	List<FieldModel> buildShortListFields();

}
