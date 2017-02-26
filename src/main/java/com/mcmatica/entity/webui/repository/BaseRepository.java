package com.mcmatica.entity.webui.repository;

import java.util.List;

import com.mcmatica.entity.webui.model.JoinModel;





//public interface  BaseMongoRepository<T,ID extends Serializable> extends MongoRepository<T, Serializable> {

	
public interface  BaseRepository<T> {

	
	
	T getById(String id);
	
	List<T> findAll();
	
	T save(T item);
	
	void delete(T item);
	
	long count();

	long count(String filter);
	

	List<T> findAll(int pageIndex, int pageSize);

	List<T> find(String filter);
	
	List<T> findSorted(String filter, List<String> properties);
	
	List<T> find(String filter, int pageIndex, int pageSize);
	
	T findOne(String filter);

	String getNextId();

	List<T> findAllSorted(List<String> properties);

	List<T> find(String filter, int pageIndex, int pageSize, List<JoinModel> joins) throws ClassNotFoundException;

	long count(String filter, List<JoinModel> joins) throws ClassNotFoundException;
	
	
	
	
}
