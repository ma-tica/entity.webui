package com.mcmatica.entity.webui.spring.converter;

import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;

import com.mcmatica.entity.webui.common.SpringContextProvider;
import com.mcmatica.entity.webui.model.BaseEntityModel;

public class McMongoIdToEntityConverter implements ConditionalGenericConverter {

	private MongoOperations mongoOperations;

	
	
	private MongoOperations getMongoOperations()
	{
		if (this.mongoOperations == null)
		{
			SpringContextProvider appContext = new SpringContextProvider();

        	this.mongoOperations = appContext.getApplicationContext().getBean(MongoOperations.class);

		}
		return this.mongoOperations;
	}

	
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, BaseEntityModel.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		if (source != null)
		{
			BasicQuery qry = new BasicQuery(String.format("{_id: '%s'}", source));
			return this.getMongoOperations().findOne(qry, targetType.getType());
		}else{
			return null;
		}
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (sourceType.getName().equals(String.class.getName())) {
			return true;
		}
		
		return false;
	}

}
