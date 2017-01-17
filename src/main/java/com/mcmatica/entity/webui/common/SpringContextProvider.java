package com.mcmatica.entity.webui.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextProvider implements ApplicationContextAware {

	 private static ApplicationContext context;
	
	 public static ApplicationContext getApplicationContext() {
	        return context;
	    }
	 
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		context = ctx;

	}

}
