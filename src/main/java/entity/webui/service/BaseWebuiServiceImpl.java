package entity.webui.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.component.panel.Panel;

import entity.webui.factory.WebuiFactory;
import entity.webui.factory.WebuiFactoryImpl;
import entity.webui.model.BaseEntityModel;
import entity.webui.model.FieldModel;
import entity.webui.repository.BaseMongoRepository;

//public abstract class BaseWebuiServiceImpl<T extends BaseEntityModel, S extends Serializable> implements BaseWebuiService<T>, WebuiFactory {
public abstract class BaseWebuiServiceImpl<T extends BaseEntityModel, S extends Serializable> implements BaseWebuiService, WebuiFactory {	

	protected WebuiFactoryImpl<T> webuiFactory;
	protected T selected;
	protected List<T> list;
	
	protected BaseMongoRepository<T, S> repository;
	
	
	@Override
	public List<T> buildList() {
		this.list = this.repository.findAll();
		return list;
	}

	@Override
	public T getSelected() {
		return this.selected;
	}

	@Override
	public void save() {
		T entity = this.repository.save(this.selected);
		list.add(entity );
	}

	@Override
	public <G extends BaseEntityModel> void setSelected(G selected) {
		this.selected =  (T) selected;		
	}

	@Override
	public T getById(String id) {
		return this.repository.getById(id);
	}

	@Override
	public <G extends BaseEntityModel> void delete(G selected) {
		this.repository.delete(this.selected);
		list.remove(this.selected);
		this.selected = null;		
	}

	@Override
	public Panel buildPanelGrid() {
		return this.webuiFactory.buildPanelGrid();		
	}

	@Override
	public List<FieldModel> buildShortListFields() {
		return this.webuiFactory.buildShortListFields();
	}



	
}
