package com.mcmatica.entity.webui.spring.converter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
public class LazyLoadAspectDecorator {

	//@Pointcut("get(@com.mcmatica.entity.webui.annotation.MCDbRef java.util.List *..*.* )")
	//@Pointcut("execution(public static * repository.entity.registry.AppImpl.getByCode(..))")
	//@Pointcut("execution(@com.mcmatica.entity.webui.annotation.MCDbRef * *..*.*.get*())")
	@Pointcut("execution(public * get*(..)) && target(com.mcmatica.entity.webui.model.BaseEntityModel)")
	//@Pointcut("execution(public * get*(..))")
	public void getterMethods()
	{
		
	}
	
	@Before("getterMethods()" )
	public void beforeEntityGetter(JoinPoint joinpoint) 
	{
		System.out.println("ASPECT J => " + joinpoint.toShortString());
		
	}

}
