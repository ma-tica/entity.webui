package com.mcmatica.entity.webui.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.ReflectionUtils;

import com.mcmatica.entity.webui.annotation.MCDbRef;
import com.mcmatica.entity.webui.common.SpringContextProvider;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class LoadLazyProvider {
	
	public static LoadLazyProvider instance = new LoadLazyProvider();
	
	private MongoOperations mongoOperations;
	
	private LoadLazyProvider() {
		
	}

	
	private MongoOperations getMongoOperations()
	{
		if (this.mongoOperations == null)
		{
			SpringContextProvider appContext = new SpringContextProvider();        	
        	this.mongoOperations = appContext.getApplicationContext().getBean(MongoOperations.class);		
		}
		return this.mongoOperations;
	}

	
	public <T extends BaseEntityModel, G extends BaseEntityModel> G loadLazyProperty(Class<G> entityType, T entity, String propertyName )
	{
		BasicDBObject query = new BasicDBObject();
		query.put("_id", entity.getId());
		Document document = entity.getClass().getAnnotation(Document.class);
		
		DBObject dbobject =  this.getMongoOperations().getCollection(document.collection()).findOne(query);

		if (dbobject != null) {
			String  id = (String) dbobject.get(propertyName);

			BasicQuery qry = new BasicQuery(String.format("{_id: '%s'}", id));
			return  this.getMongoOperations().findOne(qry, entityType );
		}

		return null;
	}



	public <T extends BaseEntityModel, G extends BaseEntityModel> List<G> loadLazyListProperty(Class<G> entityType, T entity,
																					  String propertyName )
	{
		Document from = entity.getClass().getAnnotation(Document.class);
		Document child = entityType.getAnnotation(Document.class);
		DBCollection collection = this.getMongoOperations().getCollection(from.collection());
		List<DBObject> pipeline = new ArrayList<DBObject>();
		pipeline.add(new BasicDBObject("$unwind", "$" + propertyName));
		pipeline.add(new BasicDBObject("$lookup", new BasicDBObject("from", child.collection())
				.append("localField", propertyName)
				.append("foreignField", "_id")
				.append("as", "lookupoutput")));
		pipeline.add(new BasicDBObject("$match", new BasicDBObject("_id", entity.getId())));
		
		
		List<G> resultList = new ArrayList<G>();
		
		Iterator<DBObject> result =  collection.aggregate(pipeline).results().iterator();
		while(result.hasNext())
		{
			BasicDBList tmp = (BasicDBList) result.next().get("lookupoutput");
			if (tmp != null && !tmp.isEmpty()) {
				DBObject obj = (DBObject) tmp.get(0);					
				G eobj = this.getMongoOperations().getConverter().read(entityType, obj);
				resultList.add(eobj);
			}
		}		
		
		return resultList;

	}

	public <T extends BaseEntityModel> void fullLoad(T entity)
	{

		ReflectionUtils.doWithFields(entity.getClass(), new ReflectionUtils.FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				ReflectionUtils.makeAccessible(field);

				/*
				 * Load all lazy loaded properties of an object
				 */
				MCDbRef mcdbref = field.getAnnotation(MCDbRef.class);
				if (mcdbref != null && mcdbref.lazy())
				{

					if (field.get(entity) == null) {
						if (BaseEntityModel.class.isAssignableFrom(field.getType()))
						{
							T property = loadLazyProperty((Class<T>) field.getType(), entity, field.getName());
							field.set(entity, property);
							
							
						}else if (List.class.isAssignableFrom(field.getType()))
						{
							ParameterizedType objectListType = (ParameterizedType) field.getGenericType();
							Class<T> listClass = (Class<T>) objectListType.getActualTypeArguments()[0];

							List<T> property = loadLazyListProperty(listClass, entity, field.getName());
							field.set(entity, property);
						}else{
							throw new IllegalArgumentException("Field type " + field.getType() + " not supported by entity fullLoad procedure");
						}
						
					}
				}


			}
		});

	}


}
