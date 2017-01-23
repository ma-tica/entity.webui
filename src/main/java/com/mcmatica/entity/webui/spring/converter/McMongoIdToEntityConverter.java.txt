package com.mcmatica.entity.webui.spring.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;

import com.mcmatica.entity.webui.model.BaseEntityModel;


@Component
public class  McMongoIdToEntityConverter implements ConverterFactory<String, BaseEntityModel> {

//	//
//	
//	public MongoOperations getMongoOperations() {
//		return mongoOperations;
//	}
//
//	public void setMongoOperations(MongoOperations mongoOperations) {
//		this.mongoOperations = mongoOperations;
//	}
	
	private MongoOperations mongoOperations;

	
	
	private MongoOperations getMongoOperations()
	{
		if (this.mongoOperations == null)
		{
        	ApplicationContextProvider appContext = new ApplicationContextProvider();
        	
        	this.mongoOperations = appContext.getApplicationContext().getBean(MongoOperations.class);

			
		}
		return this.mongoOperations;
	}
	

	
	
	@Override
	public <T extends BaseEntityModel> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToEntityConverter<T>(targetType, this.getMongoOperations());
	}

	private final class StringToEntityConverter<T extends BaseEntityModel> implements Converter<String, T> {

        private Class<T> entityType;

        private MongoOperations mongoOperations;
        

        public StringToEntityConverter(Class<T> entityType, MongoOperations mongoOperations) {
            this.entityType = entityType;
            this.mongoOperations = mongoOperations;
        }

        public T convert(String source) {
            //return (T) Enum.valueOf(this.enumType, source.trim());             
			
        	
			BasicQuery qry = new BasicQuery(String.format("{_id: '%s'}", source));
		//	return null;
			
			return this.mongoOperations.findOne(qry, entityType);	

        }
    }

}
