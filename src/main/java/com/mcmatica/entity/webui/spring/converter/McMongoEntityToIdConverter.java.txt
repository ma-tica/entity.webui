package com.mcmatica.entity.webui.spring.converter;

import org.springframework.core.convert.converter.Converter;

import com.mcmatica.entity.webui.model.BaseEntityModel;

public class McMongoEntityToIdConverter implements Converter<BaseEntityModel, String> {

	@Override
	public String convert(BaseEntityModel source) {
		
		return source.getId();
	}


}
