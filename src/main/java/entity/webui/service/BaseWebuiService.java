package entity.webui.service;

import java.util.List;

import org.primefaces.component.panel.Panel;

import entity.webui.model.BaseEntityModel;
import entity.webui.model.FieldModel;



//public interface BaseWebuiService<T extends BaseEntityModel> {
public interface BaseWebuiService {

	<T extends BaseEntityModel> List<T> buildList();
	
	Panel buildPanelGrid();
	
	List<FieldModel> buildShortListFields();
	
	<T extends BaseEntityModel> T getSelected();

	void save();

	<T extends BaseEntityModel> void setSelected(T selected);

	<T extends BaseEntityModel> T getById(String id);

	<T extends BaseEntityModel> void delete(T selected);
	
	<T extends BaseEntityModel> T create();
	
	void setDefaultValue();
	
	<T extends BaseEntityModel> List<T> findAll();
	
}
