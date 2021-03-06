package com.geewaza.wangheng.spider.shanbay.wordbook.main;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.geewaza.wangheng.spider.shanbay.wordbook.config.ApplicationManager;
import com.geewaza.wangheng.spider.shanbay.wordbook.service.WordbookSpiderService;

public class ShanbayWordbookSpiderMain {
	
	public static void main(String[] args) throws Exception {
		
		URL url = ShanbayWordbookSpiderMain.class.getResource("/log4j.properties");
		PropertyConfigurator.configure(url);
		
		Logger logger = LoggerFactory.getLogger(ShanbayWordbookSpiderMain.class);
		ApplicationManager.initApplicationContext(new ClassPathXmlApplicationContext("applicationContext.xml"));
		WordbookSpiderService service = ApplicationManager.getBean("wordbookSpiderService", WordbookSpiderService.class);
		System.out.println("开始");
		long start = System.currentTimeMillis();
		try {
//			service.loadWordbookDataToFile();
			service.loadWordbookDataToDB();
		} finally {
			logger.info("耗时：" + (System.currentTimeMillis() - start) + "ms.");
			System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms.");
		}
	}
}
