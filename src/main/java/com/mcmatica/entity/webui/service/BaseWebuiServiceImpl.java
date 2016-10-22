package com.mcmatica.entity.webui.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;

import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.factory.WebuiFactory;
import com.mcmatica.entity.webui.factory.WebuiFactoryImpl;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.FieldModel;
import com.mcmatica.entity.webui.repository.BaseMongoRepository;

public abstract class BaseWebuiServiceImpl<T extends BaseEntityModel, S extends Serializable> implements BaseWebuiService, WebuiFactory {	

	protected WebuiFactoryImpl<T> webuiFactory;
	protected T selected;
	protected T originalSelected;
	protected List<T> list;
	
	protected BaseMongoRepository<T, S> repository;
	
	@SuppressWarnings("unchecked")
	public T create()
	{
		T entity = this.getInstanceOfT();
		this.setSelected(entity);
		this.selected.setNewInstanceState(true);
		return this.selected;
	}

	
	@SuppressWarnings("unchecked")
	T getInstanceOfT()
    {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
        try
        {
            return type.newInstance();
        }
        catch (Exception e)
        {
            // Oops, no default constructor
            throw new RuntimeException(e);
        }
    }
	
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
		if (this.selected.isNewInstance())
		{
			list.add(entity );
			
		}
		entity.setNewInstanceState(false);
		this.restIsEditing();		
	}

	@Override
	public <G extends BaseEntityModel> void setSelected(G selected) {
		this.selected =  (T) selected;	
		/**
		 * Save the original value of selected Item
		 */
//		if (this.originalSelected == null) {
			this.restIsEditing();
//		}
	}

	@Override
	public T getById(String id) {
		return this.repository.getById(id);
	}

	@Override
	public <G extends BaseEntityModel> void delete(G selected) {
		this.repository.delete(this.selected);
		list.remove(this.selected);
		this.setSelected (null);		
	}

	@Override
	public Panel buildPanelGrid() {
		return this.webuiFactory.buildPanelGrid();		
	}

	@Override
	public DataTable buildSelectionGrid() {
		return this.webuiFactory.buildSelectionGrid();
	}

	
//	@Override
//	public List<FieldModel> buildShortListFields() {
//		return this.webuiFactory.buildShortListFields();
//	}

	@Override
	public List<FieldModel> getFields() {
		return this.webuiFactory.getFields();
	}
	
	
	@Override
	public void setDefaultValues()
	{	
		for(FieldModel fmodel : this.getFields())
		{
			if (fmodel.getDefaultValue() != null && !fmodel.getDefaultValue().isEmpty())
			{
				
				try {
					Method method = this.getSelected().getClass().getMethod("set" + Utility.capitalize(fmodel.getPropertyName()), fmodel.getPropertyType());
					method.invoke(this.getSelected(), Utility.createValue(fmodel.getDefaultValue(), fmodel.getPropertyType()));
					
				} catch (Exception e) {					
					e.printStackTrace();
					throw new RuntimeException(e);
				} 
				
			}
		}

	}

	@Override
	public List<T> findAll()
	{
		return this.repository.findAll();
	}

	@Override
	public boolean isEditing()
	{
		boolean result = false;
		try {
			if (Utility.areEquals(this.originalSelected, this.getSelected()))
			{
				result = false;
			}else{
				result = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private void restIsEditing()
	{
		try {
			if (this.selected != null) {
				this.originalSelected = Utility.cloneEntity(this.selected);
			}else{
				this.originalSelected = null;
			}
		} catch (Exception e) {				
			e.printStackTrace();
		}
		
	}
	
}
