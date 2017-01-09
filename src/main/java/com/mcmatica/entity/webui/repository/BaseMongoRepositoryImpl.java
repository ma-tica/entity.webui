package com.mcmatica.entity.webui.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mcmatica.entity.webui.model.BaseEntityModel;

public class BaseMongoRepositoryImpl<T extends BaseEntityModel> implements BaseRepository<T> {

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
	public List<T> findAll() {
		return this.operations.findAll(this.clazz);
	}

	@Override
	public T save(T item) {
		this.operations.save(item);
		return item;
	}

	@Override
	public void delete(T item) {
		this.operations.remove(item);		
	}

	@Override
	public long count()
	{
		Query query = new Query();
		return this.operations.count(query, this.clazz);
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
	public T findOne(String filter) {
		
		BasicQuery query = new BasicQuery(filter);			
		
		return this.operations.findOne(query, this.clazz);
	}

	
	
	@Override
	public List<T> find(String filter, int pageIndex, int pageSize) {
		BasicQuery query = new BasicQuery(filter);			
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		query.with(pageable);		
		return this.operations.find(query, this.clazz);
	}

	@Override
	public long count(String filter) {
		// TODO Auto-generated method stub
		return 0;
	}

}
