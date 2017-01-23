package com.mcmatica.entity.webui.spring.converter;

import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import com.mcmatica.entity.webui.model.BaseEntityModel;

public class McMongoEntityToIdConverter implements GenericConverter {


	
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(BaseEntityModel.class, String.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		
		return ((BaseEntityModel) source).getId();
		
	}


}
