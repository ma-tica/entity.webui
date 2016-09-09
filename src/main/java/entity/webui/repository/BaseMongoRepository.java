package entity.webui.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;





public interface  BaseMongoRepository<T,ID extends Serializable> extends MongoRepository<T, Serializable> {

	T getById(String id);
	
}
