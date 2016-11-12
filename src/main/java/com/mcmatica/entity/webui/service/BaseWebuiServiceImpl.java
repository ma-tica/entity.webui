package com.mcmatica.entity.webui.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import com.mcmatica.entity.webui.annotation.MCCascadeSave;
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
	@Override
	public T create()
	{
		T entity = this.getInstanceOfT();
		this.setSelected(entity);
		this.selected.setNewInstanceState(true);
		return this.selected;
	}

	
	
	@SuppressWarnings("unchecked")	
	public T getInstanceOfT()
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
	public void cancel()
	{
		this.setSelected(this.repository.getById(this.selected.getId()));
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
	public void delete() {
		
		//Cascade delete
		execSaveBeforeDelete = false;
		this.performeCascadeDelete(this.selected);		
		if (execSaveBeforeDelete)
		{
			this.repository.save(this.selected);
		}
		
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

	Boolean execSaveBeforeDelete = false;
	
	private <G extends BaseEntityModel> void performeCascadeDelete(G source )
	{
		
		ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				ReflectionUtils.makeAccessible(field);

				if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(MCCascadeSave.class)) {
				MCCascadeSave cascadeSave = field.getAnnotation(MCCascadeSave.class);
				if (cascadeSave.cascadeDelete())
				{
					/*
					 * Execute Cascade Deleting
					 */
					//cascadeDeleting(field, (BaseEntityModel) event.getDBObject());
					prepareCascadeDeleting(field, source);
					execSaveBeforeDelete = true;
				}
			}
				
			}
		});
	}
	
	
	private <G extends BaseEntityModel> void prepareCascadeDeleting(Field field, BaseEntityModel source)
			throws IllegalArgumentException, IllegalAccessException {
		if (field.getType().equals(List.class)) {
			List<G> items = (List) field.get(source);

			// //Type elementType = ((ParameterizedType)
			// field.getGenericType()).getActualTypeArguments()[0];
			// Iterator iterator = field. ((List) field).iterator();
			// while(iterator.hasNext())
			// {

			for (G listItem : items)

				source.getFieldListItemsRemoved(field.getName()).add(listItem);

		}

	}

}
