package com.mcmatica.entity.webui.spring.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LongToLong implements Converter<Long, Long> {

	@Override
	public Long convert(Long source) {
		

		return source;
	}

}
