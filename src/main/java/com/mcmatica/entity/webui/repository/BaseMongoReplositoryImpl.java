package com.mcmatica.entity.webui.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mcmatica.entity.webui.model.BaseEntityModel;

public  class  BaseMongoReplositoryImpl<T extends BaseEntityModel > implements BaseMongoRepository<T> {

	protected MongoOperations operations;
	
	private Class<T> clazz;
	
	public BaseMongoReplositoryImpl(Class<T> clazz, MongoOperations operations) {
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
		Query qry = new Query();
		return this.operations.count(qry, this.clazz);
	}
	
	@Override
	public List<T> findAll(Pageable pageable)
	{
		Query qry = new Query();
		qry.with(pageable);
		return this.operations.find(qry, this.clazz);
	}

	@Override
	public List<T> find(Query query) {
		 return this.operations.find(query, this.clazz);
	}
}
