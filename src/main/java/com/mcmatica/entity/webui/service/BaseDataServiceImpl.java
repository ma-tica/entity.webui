package com.mcmatica.entity.webui.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import com.mcmatica.entity.webui.annotation.MCCascadeSave;
import com.mcmatica.entity.webui.common.SpringContextProvider;
import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.BaseEntityDataModel;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.scanner.FieldModel;
import com.mcmatica.entity.webui.repository.BaseRepository;


public class BaseDataServiceImpl<T extends BaseEntityModel, R extends BaseRepository<T>> implements BaseDataService<T> {

	protected BaseRepository<T> repository;
	protected T selected;
	protected BaseEntityDataModel<T> list;
	
	private boolean isEditing;

	Boolean execSaveBeforeDelete = false;

	private Logger logger = LogManager.getLogger(this.getClass().getName());

//	public BaseDataServiceImpl(BaseRepository<T> repository) {
//		this.repository = repository;
//	}

	public BaseDataServiceImpl(Class<R> clazz) {
		if (this.repository == null)
		{
			ApplicationContext springContext = SpringContextProvider.getApplicationContext();
			this.repository = springContext.getBean(clazz);
		}
	}

	@Override
	public void save() {
		
		T entity = this.repository.save(this.selected);
		if (this.selected.isNewInstance())
		{
			this.list.add(entity );
			
		}
		entity.setNewInstanceState(false);
		this.stopEditing();		
	}

	@Override
	public void delete() {
		
		//Cascade delete
		this.execSaveBeforeDelete = false;
		this.performeCascadeDelete(this.selected);		
		if (execSaveBeforeDelete)
		{
			this.repository.save(this.selected);
		}
		
		this.repository.delete(this.selected);
		this.list.remove(this.selected);
		this.setSelected (null);		
		
		this.stopEditing();
	}

	
	
	private void performeCascadeDelete(T source )
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
	
	
	private  void prepareCascadeDeleting(Field field, BaseEntityModel source)
			throws IllegalArgumentException, IllegalAccessException {
		if (field.getType().equals(List.class)) {
			List<T> items = (List) field.get(source);

			// //Type elementType = ((ParameterizedType)
			// field.getGenericType()).getActualTypeArguments()[0];
			// Iterator iterator = field. ((List) field).iterator();
			// while(iterator.hasNext())
			// {

			for (T listItem : items)

				source.getFieldListItemsRemoved(field.getName()).add(listItem);

		}

	}

	@Override
	public T create()
	{
		T entity = this.getInstanceOfT();
		this.setSelected(entity);
		this.selected.setNewInstanceState(true);
		
		this.startEditing();
		
		return this.selected;
	}
	

	private T getInstanceOfT()
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
	public void setSelected(BaseEntityModel selected) {

		BaseEntityModel selected0 = null;
		if (AopUtils.isJdkDynamicProxy(selected))
		{
			/*
			 * obtain the target object behind the Proxy
			 */
			try {
				selected0 = (T) ((org.springframework.aop.framework.Advised)selected).getTargetSource().getTarget();
			} catch (Exception e) {
				logger.error(e);
				return;
			}
		}else
		{
			selected0 = selected;
		}

		
		try {
			Utility.loadLazyEntityProperties(selected0);
		} catch (Exception e) {
			
			logger.error(e);
		}
		
		this.selected =  (T) selected0;
		
		/**
		 * Save the original value of selected Item
		 */
//		if (this.originalSelected == null) {
			this.stopEditing();
//		}
	}

	
	@Override
	public T getSelected() {
		return this.selected;
	}

	@Override
	public T getById(String id) {
		return this.repository.getById(id);
	}
	
	@Override
	public BaseEntityDataModel<T> buildList() {
		if (this.list == null)
		{
			this.list = new BaseEntityDataModel<T>(this.repository);
		}
		return list;
	}
	
	@Override
	public List<T> findAll()
	{
		return this.repository.findAll();
	}


	@Override
	public List<T> find(String filter)
	{
		return this.repository.find(filter);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> findSorted(String filter, List properties)
	{
		return this.repository.findSorted(filter, properties);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> findAllSorted(List properties) {
		return this.repository.findAllSorted(properties);
	}
	
	
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setDefaultValues(List fieldModels)
	{	
		for (int i = fieldModels.size() - 1; i >= 0; i--) {
			FieldModel fmodel = (FieldModel) fieldModels.get(i);
		
		
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
	public boolean isEditing() {
		return this.isEditing;
	}

	@Override
	public void cancel() {
		this.stopEditing();
		
//		//this.setSelected(this.repository.getById(this.selected.getId()));
		
		try {
			/*
			 * this.selected.getIdValue() <= 0 means we are creating a new object
			 * ad we decide to abort the creation
			 */
			if (this.selected.getIdValue() < 0)
			{
				this.selected = null;
//				this.originalSelected = null;
			}else
			{
				Utility.copyEntity(this.repository.getById(this.selected.getId()), this.selected );
			}
		} catch (Exception e) {
			logger.error(e);
		}		
		
//		this.list.setRowData(this.selected);
		
	}

	@Override
	public void startEditing() {
		this.isEditing = true;
	}
	
	
	
	private void stopEditing()
	{
		this.isEditing = false;
	}



	
}
