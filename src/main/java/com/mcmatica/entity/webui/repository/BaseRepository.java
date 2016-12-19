package com.mcmatica.entity.webui.repository;

import java.util.List;





//public interface  BaseMongoRepository<T,ID extends Serializable> extends MongoRepository<T, Serializable> {

	
public interface  BaseRepository<T> {

	
	
	T getById(String id);
	
	List<T> findAll();
	
	T save(T item);
	
	void delete(T item);
	
	long count();

	long count(String filter);
	
	//List<T> findAll(Pageable pageable);
	List<T> findAll(int pageIndex, int pageSize);

	//List<T> find(Query query);
	List<T> find(String filter);
	
	List<T> find(String filter, int pageIndex, int pageSize);
	
	
	
	
	
}
