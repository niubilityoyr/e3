package com.e3mall.sso.spring;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.e3mall.sso.service.RegisterService;
import com.e3mall.sso.service.impl.RegisterServiceImpl;

public class SpringTest {
	
	@Test
	public void test1() throws IOException{
		ApplicationContext applicationContext = new 
				ClassPathXmlApplicationContext("classpath:spring/applicationContext-service.xml");
		RegisterService registerService = 
				(RegisterService) applicationContext.getBean(RegisterServiceImpl.class);
		System.in.read();
	}
}
