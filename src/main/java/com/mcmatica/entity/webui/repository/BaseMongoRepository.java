package com.mcmatica.entity.webui.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;





//public interface  BaseMongoRepository<T,ID extends Serializable> extends MongoRepository<T, Serializable> {

	
public interface  BaseMongoRepository<T> {

	
	
	T getById(String id);
	
	List<T> findAll();
	
	T save(T item);
	
	void delete(T item);
	
	long count();

	List<T> findAll(Pageable pageable);

	List<T> find(Query query);
	
	
}
