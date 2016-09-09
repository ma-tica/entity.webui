package entity.webui.factory;

import java.util.List;

import javax.faces.component.html.HtmlPanelGrid;

import entity.webui.model.FieldModel;

public interface WebuiFactory {

	HtmlPanelGrid buildPanelGrid();

	List<FieldModel> buildFields();

}
