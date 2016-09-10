package entity.webui.factory;

import java.util.List;

import javax.faces.component.html.HtmlPanelGrid;

import org.primefaces.component.panelgrid.PanelGrid;

import entity.webui.model.FieldModel;

public interface WebuiFactory {

	PanelGrid buildPanelGrid();

	List<FieldModel> buildShortListFields();

}
