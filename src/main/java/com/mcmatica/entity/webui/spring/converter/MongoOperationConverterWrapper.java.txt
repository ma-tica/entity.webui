package com.mcmatica.entity.webui.spring.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

public class MongoOperationConverterWrapper {

	private MongoOperations mongoOperations;
	
	private static MongoOperationConverterWrapper MONGROOPERAION = new MongoOperationConverterWrapper();
	
	private MongoOperationConverterWrapper() {
		
	}
	
	public static MongoOperationConverterWrapper getInstance() {
		return MONGROOPERAION;
	}
	
	public MongoOperations getMongoOperations()
	{
		return this.mongoOperations;
	}
	
}
