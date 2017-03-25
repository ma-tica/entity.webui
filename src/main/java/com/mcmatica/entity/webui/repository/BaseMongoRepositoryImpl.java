package com.mcmatica.entity.webui.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mcmatica.entity.webui.common.Constant;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.scanner.JoinModel;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class BaseMongoRepositoryImpl<T extends BaseEntityModel> implements BaseRepository<T> {

	Logger logger = LogManager.getLogger(BaseMongoRepositoryImpl.class);
	
	
	protected MongoOperations operations;
	private Class<T> clazz;
	
	
	
	public BaseMongoRepositoryImpl(Class<T> clazz, MongoOperations operations) {
		this.clazz = clazz;
		this.operations = operations;
	}
	
	@Override
	public T getById(String id) {
		Query qry = new Query();
		qry.addCriteria(new Criteria("Id").is(id));
		List<T> result = this.operations.find(qry, this.clazz);
		if (result != null && !result.isEmpty())
		{
			return result.get(0);
		}else{
			return null;
		}
	}


	
	@Override
	public T save(T item) {
		this.operations.save(item);
		return item;
	}

	@Override
	public void delete(T item) {
		WriteResult result =  this.operations.remove(item);
		if (logger.isInfoEnabled())
		{
			logger.info("Deleted count: " + result.getN());
		}
		
	}

	@Override
	public long count()
	{
		Query query = new Query();
		return this.operations.count(query, this.clazz);
	}

	@Override
	public long count(String filter) {
		BasicQuery query = new BasicQuery(filter);
		return this.operations.count(query, this.clazz);
	}

	@Override
	public long count(String filter, List<JoinModel> joins) throws ClassNotFoundException {
		BasicQuery query = new BasicQuery(filter);
		
		BasicDBObject match = new BasicDBObject();
	    match.putAll(query.getQueryObject());
	    List<DBObject> pipeline = new ArrayList<DBObject>();
		
		
		for (JoinModel join : joins) {
			
			String collectionname = this.operations.getCollectionName(Class.forName(join.getEntityClassName()));
			
			pipeline.add(new BasicDBObject("$lookup", new BasicDBObject("from", collectionname)
				.append("localField", join.getLocalFieldName())
				.append("foreignField", join.getForeignFieldName())
				.append("as", join.getAliasName())));
		}
		pipeline.add(new BasicDBObject("$match", match));


		pipeline.add(new BasicDBObject("$group", new BasicDBObject("_id", null).append("count", new BasicDBObject("$sum", 1)) ));
		
		if (logger.isInfoEnabled()) 
		{
			logger.info(pipeline);
		}
		
		DBCollection collection = this.operations.getCollection(this.operations.getCollectionName(this.clazz));
		
		Long count = (long)0;
		Iterator<DBObject> result =  collection.aggregate(pipeline).results().iterator();
		while(result.hasNext())
		{
			
			DBObject obj = result.next();
			
			count = Long.parseLong(obj.get("count").toString());
			
			
		}		
		
		return count;

	
	}

	@Override
	public List<T> findAll(int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex, pageSize);		
		
		Query query = new Query();
		query.with(pageable);
		return this.operations.find(query, this.clazz);

	}

	@Override
	public List<T> find(String filter) {
		
		BasicQuery query = new BasicQuery(filter);					
		return this.operations.find(query, this.clazz);
		
	}

	@Override
	public List<T> findAll() {
		
		return this.operations.findAll(this.clazz);
	}

	
	@Override
	public List<T> findAllSorted(List<String> properties) {
		
		Sort sort = new Sort(Direction.ASC, properties);
		
		Query qry = new Query();
		qry.with(sort);
		
		
		return this.operations.find(qry, this.clazz);
	}

	@Override
	public List<T> findAllSorted(String ... properties) {
		
		Sort sort = new Sort(Direction.ASC, properties);
		
		Query qry = new Query();
		qry.with(sort);
		
		
		return this.operations.find(qry, this.clazz);
	}

	
	@Override
	public List<T> findSorted(String filter, List<String> properties) {
		
		BasicQuery query = new BasicQuery(filter);			
		
		Sort sort = new Sort(Direction.ASC, properties);
		
		query.with(sort);		
		
		return this.operations.find(query, this.clazz);
	}

	
	@Override
	public T findOne(String filter) {
		
		BasicQuery query = new BasicQuery(filter);			
		
		return this.operations.findOne(query, this.clazz);
	}

	
	@Override
	public List<T> find(String filter, int pageIndex, int pageSize, List<JoinModel> joins) throws ClassNotFoundException {
		BasicQuery query = new BasicQuery(filter);
				
		BasicDBObject match = new BasicDBObject();
	    match.putAll(query.getQueryObject());
	    List<DBObject> pipeline = new ArrayList<DBObject>();
		
		
		for (JoinModel join : joins) {
			
			String collectionname = this.operations.getCollectionName(Class.forName(join.getEntityClassName()));
			
			pipeline.add(new BasicDBObject("$lookup", new BasicDBObject("from", collectionname)
				.append("localField", join.getLocalFieldName())
				.append("foreignField", join.getForeignFieldName())
				.append("as", join.getAliasName())));
		}
		pipeline.add(new BasicDBObject("$match", match));

		pipeline.add(new BasicDBObject("$skip", pageIndex * pageSize));
		pipeline.add(new BasicDBObject("$limit", pageSize));

		pipeline.add(new BasicDBObject("$project", new BasicDBObject("_id", 1) ));
		
		if (logger.isInfoEnabled()) 
		{
			logger.info(pipeline);
		}
		
		DBCollection collection = this.operations.getCollection(this.operations.getCollectionName(this.clazz));
		
		List<T> resultList = new ArrayList<T>();
		Iterator<DBObject> result =  collection.aggregate(pipeline).results().iterator();
		while(result.hasNext())
		{
			
			DBObject obj = result.next();
			
			Query qry = new Query();
			qry.addCriteria(Criteria.where("_id").is(obj.get("_id")));
			
			T eobj = this.operations.findOne(qry, this.clazz);
			resultList.add(eobj);
			
		}		
		
		return resultList;
	
	}
	
	@Override
	public List<T> find(String filter, int pageIndex, int pageSize) {
		
		BasicQuery query = new BasicQuery(filter);			
		Pageable pageable = new PageRequest(pageIndex, pageSize);		
		query.with(pageable);		
		return this.operations.find(query, this.clazz);
		
	}

	
	@Override
	public String getNextId()
	{
		BasicQuery qry = new BasicQuery("{}");
		qry.setSortObject(new BasicDBObject("_id", -1));
		
		T result = this.operations.findOne(qry, this.clazz);
		
		Long id = 0l;
		if (result != null) {
			id = Long.parseLong(result.getId());			
		}
		id = id +1;
		
		return  String.format("%0" + Constant.ID_LENGTH + "d", id);
	}


}
