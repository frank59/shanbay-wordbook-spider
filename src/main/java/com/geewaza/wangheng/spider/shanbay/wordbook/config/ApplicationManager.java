package com.geewaza.wangheng.spider.shanbay.wordbook.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class ApplicationManager {

	private static ApplicationContext applicationContext;
	
	public static void initApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}
	
	public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return applicationContext.getBean(name, requiredType);
	}
	
}
