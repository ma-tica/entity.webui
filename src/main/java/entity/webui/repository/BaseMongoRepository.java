package entity.webui.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;





public interface  BaseMongoRepository<T,ID extends Serializable> extends MongoRepository<T, Serializable> {
//public interface  BaseMongoRepository<T,ID extends Serializable> extends PagingAndSortingRepository<T, Serializable> {

	T getById(String id);
	
	List<T> findAll();
	
}
