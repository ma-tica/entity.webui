package com.mcmatica.entity.webui.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.BSONObject;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.spring.converter.ApplicationContextProvider;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class LoadLazyProvider {
	
	public static LoadLazyProvider istance = new LoadLazyProvider();
	
	private MongoOperations mongoOperations;
	
	private LoadLazyProvider() {
		
	}

	
	private MongoOperations getMongoOperations()
	{
		if (this.mongoOperations == null)
		{
        	ApplicationContextProvider appContext = new ApplicationContextProvider();        	
        	this.mongoOperations = appContext.getApplicationContext().getBean(MongoOperations.class);		
		}
		return this.mongoOperations;
	}

	
	public <T extends BaseEntityModel, G extends BaseEntityModel> G loadLazy(Class<G> entityType, T entity, String propertyName )
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
	
	public <T extends BaseEntityModel, G extends BaseEntityModel> List<G> listLoadLazy(Class<G> entityType, BaseEntityModel entity, 																					  
																					  String propertyName, BasicDBObject match )
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
		pipeline.add(new BasicDBObject("$match", match.append("_id", entity.getId())));
		
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
	
	public <T extends BaseEntityModel, G extends BaseEntityModel> List<G> listLoadLazy(Class<G> entityType, BaseEntityModel entity, String propertyName )
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
		
		
//		class AggResultObj {
//			
//			String _id;
//			List<G> lookupoutput;
//			public String get_id() {
//				return _id;
//			}
//			public void set_id(String _id) {
//				this._id = _id;
//			}
//			public List<G> getLookupoutput() {
//				return lookupoutput;
//			}
//			public void setLookupoutput(List<G> lookupoutput) {
//				this.lookupoutput = lookupoutput;
//			}
//
//		}
//		
//		
//		Aggregation aggregation = Aggregation.newAggregation(
//				Aggregation.match(Criteria.where("_id").is(entity.getId())),
//			    Aggregation.unwind(propertyName),
//			    Aggregation.lookup(list.collection(), propertyName, "_id", "lookupoutput"),
//			    Aggregation.project("lookupoutput")
//			);
////	    Aggregation.group("lookup-output")
//
//		
//		System.out.println("Query  ==>>["+aggregation.toString()+"]");
//		AggregationResults<AggResultObj> data =   this.getMongoOperations().aggregate(aggregation, from.collection(), AggResultObj.class);
//		//AggregationResults<T> data =   (AggregationResults<T>) this.getMongoOperations().aggregate(aggregation, from.collection(), entity.getClass());
//		
//		System.out.println(data.getMappedResults());
//		System.out.println(data.getRawResults().get("result"));
//		return (List<G>) data.getMappedResults();
		
//		DBObject match = new BasicDBObject();
//		match.put("$match", new BasicDBObject().put("_id",entity.getId()));
//		DBObject lookup = new BasicDBObject();
//		lookup.put("$lookup", new BasicDBObject().put("from", list.collection()) )
//		
//		this.getMongoOperations().getCollection(from.collection()).aggregate(pipeline)
		

//		Object  object = dbobject.get(propertyName);
//		
//		List<G> result = new ArrayList<G>();
//		if (object instanceof List)
//		{
//			List<String> list = (List<String>) object;
//			 
//			for(String id : list)
//			{
//				BasicQuery qry = new BasicQuery(String.format("{_id: '%s'}", id));
//				G entitytmp  =  this.getMongoOperations().findOne(qry, entityType );
//				result.add(entitytmp);
//			}
//		}
//		
//		
//		return result;
		
//		T entitytmp  =  this.getMongoOperations().findOne(qry, entityType );
//		
//		java.lang.reflect.Method method;
//		try {
//			method = entitytmp.getClass().getMethod("get" + Utility.capitalize(propertyName));
//			return (List<G>) method.invoke(entitytmp);
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
		
	}
	

}
