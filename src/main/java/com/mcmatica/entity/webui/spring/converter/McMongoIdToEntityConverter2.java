package com.mcmatica.entity.webui.spring.converter;

import java.util.Collections;
import java.util.Set;

import org.springframework.cglib.proxy.Proxy;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;

import com.mcmatica.entity.webui.model.BaseEntityModel;

public class McMongoIdToEntityConverter2 implements ConditionalGenericConverter {

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
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, Proxy.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		
//		System.out.println(targetType.getType().getSimpleName() + " : " + (String) source);

//		BaseEntityModel dummy = null;
//		try {
//			dummy = (BaseEntityModel) targetType.getType().newInstance();
//			dummy.setId((String) source); 
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		
//		return dummy;
		
		BasicQuery qry = new BasicQuery(String.format("{_id: '%s'}", source));
		
		
		return this.getMongoOperations().findOne(qry, targetType.getType());	
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		
		
		
		if (sourceType.getName().equals(String.class.getName())  ) {
			
			
			return true;
			
		}
		
		return false;
	}

}
