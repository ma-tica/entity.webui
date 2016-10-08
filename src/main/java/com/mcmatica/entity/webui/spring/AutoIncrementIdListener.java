package com.mcmatica.entity.webui.spring;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.ReflectionUtils;

import com.mcmatica.entity.webui.common.Constant;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mongodb.BasicDBObject;

public class AutoIncrementIdListener extends AbstractMongoEventListener<BaseEntityModel> {

	@Autowired
    private MongoOperations mongoOperations;

	
	
	
	@Override
	public void onBeforeConvert(BeforeConvertEvent<BaseEntityModel> event) {

		System.out.println("onBeforeConvert: {}:"+ event.getSource());

		for ( Field field : event.getSource().getClass().getDeclaredFields())
		{
			if (field.getAnnotation(Id.class) != null)
			{
				try {
					ReflectionUtils.makeAccessible(field);
					
					if (field.get(event.getSource()) == null || field.get(event.getSource()).toString().isEmpty())
					{
						String nextid = this.nextId(event.getSource());

						field.set(event.getSource(), nextid);
					}
					break;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
		super.onBeforeConvert(event);
	}


	@Override
	public void onAfterConvert(AfterConvertEvent<BaseEntityModel> event) {
		System.out.println("onAfterConvert: {}:"+ event.getSource());
		super.onAfterConvert(event);
	}

	
//	@Override
//	public void onBeforeSave(BeforeSaveEvent<BaseEntityModel> event) {
//		for ( Field field : event.getSource().getClass().getDeclaredFields())
//		{
//			if (field.getAnnotation(Id.class) != null)
//			{
//				try {
//					ReflectionUtils.makeAccessible(field); 
//					
//					
//					if (field.get(event.getSource()) == null || field.get(event.getSource()).toString().isEmpty())
//					{
//
//						String nextid = this.nextId(event.getSource());
//						
//						field.set(event.getSource(), nextid);
//						break;
//					}
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		
//		
//		super.onBeforeSave(event);
//	}
	
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
