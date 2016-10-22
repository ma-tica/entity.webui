package com.mcmatica.entity.webui.spring;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.ReflectionUtils;

import com.mcmatica.entity.webui.annotation.MCCascadeSave;
import com.mcmatica.entity.webui.annotation.MCCreatedOn;
import com.mcmatica.entity.webui.annotation.MCUpdatedOn;
import com.mcmatica.entity.webui.common.Constant;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mongodb.BasicDBObject;


public class McMongoEventListener extends AbstractMongoEventListener<BaseEntityModel> {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public void onBeforeConvert(final BeforeConvertEvent<BaseEntityModel> event) {
		ReflectionUtils.doWithFields(event.getSource().getClass(), new ReflectionUtils.FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				ReflectionUtils.makeAccessible(field);
				
				/*
				 * Id auto-increment value
				 */
				if (field.getAnnotation(Id.class) != null)				
				{
					if (field.get(event.getSource()) == null || field.get(event.getSource()).toString().isEmpty()) {
						String nextid = nextId(event.getSource());
						field.set(event.getSource(), nextid);
					}
				}

				/*
				 * Created On
				 */
				if (field.getAnnotation(MCCreatedOn.class) != null)
				{
					if (field.get(event.getSource()) == null ) {
						field.set(event.getSource(), new Date());
					}
				}

				/*
				 * Updated On
				 */
				if (field.getAnnotation(MCUpdatedOn.class) != null)
				{
					field.set(event.getSource(), new Date());
				}
				
				
				/*
				 * Execute Cascade Saving
				 */				
				if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(MCCascadeSave.class)) {
					cascadeSaving(field, event.getSource());
					MCCascadeSave cascadeSave = field.getAnnotation(MCCascadeSave.class);
					if (cascadeSave.cascadeDelete())
					{
						/*
						 * Execute Cascade Deleting
						 */
						cascadeDeleting(field, event.getSource());
					}
				}
			}
		});
	}
	
	
	private void cascadeSaving(Field field, BaseEntityModel source) throws IllegalArgumentException, IllegalAccessException {
		
			final Object fieldValue =  field.get(source);
			if (fieldValue == null) {
				return;
			}
			ContainsIdCallback callback = new ContainsIdCallback();
			
			if (field.getType().equals(List.class))
			{
				Type elementType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
				ReflectionUtils.doWithFields((Class) elementType, callback);
				if (!callback.isIdFound()) {
					throw new MappingException("Cannot perform cascade save on child object without id set");
				}
				
				Iterator iterator = ((List) fieldValue).iterator();
				while(iterator.hasNext())
				{
					Object listItem = iterator.next();
					mongoOperations.save(listItem);
				}

			}else {

				ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
				if (!callback.isIdFound()) {
					throw new MappingException("Cannot perform cascade save on child object without id set");
				}
				mongoOperations.save(fieldValue);
			}
		
		
	}
	
	
	private void cascadeDeleting(Field field, BaseEntityModel source) throws IllegalArgumentException, IllegalAccessException {
		if (field.getType().equals(List.class)) {
			//Type elementType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
			Iterator iterator = source.getFieldListItemsRemoved(field.getName()).iterator();
			while(iterator.hasNext())
			{
				Object listItem = iterator.next();
				mongoOperations.remove(listItem);				
			}
			/*
			 * Clear the removed item list
			 */
			source.getFieldListItemsRemoved(field.getName()).clear();
			
		}
			
	}
	
	private static class ContainsIdCallback implements ReflectionUtils.FieldCallback {
		private boolean idFound;

		public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
			ReflectionUtils.makeAccessible(field);

			if (field.isAnnotationPresent(Id.class)) {
				idFound = true;
			}
		}

		public boolean isIdFound() {
			return idFound;
		}
	}

	private <T extends BaseEntityModel> String nextId(T source)
	{
		
		BasicQuery qry = new BasicQuery("{}");
		qry.setSortObject(new BasicDBObject("_id", -1));
		
		BaseEntityModel result = mongoOperations.findOne(qry, source.getClass());
		
		Long id = Long.parseLong(result.getId());
		id = id +1;
		
		return  String.format("%0" + Constant.ID_LENGTH + "d", id);
	}
	
}
