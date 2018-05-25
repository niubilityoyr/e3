package com.e3mall.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;


public class ActiveMqSpringTest {
	
	//生产者
	@Test
	public void test1(){
		//加载spring容器
		ApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//从spring容器中获得JmsTemplate对象
		JmsTemplate template = applicationContext.getBean(JmsTemplate.class);
		//从spring容器中取Destination对象
		Destination destination = (Destination) applicationContext.getBean("queueDestination");
		//使用JmsTemplate对象发送消息。
		template.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("send message");
			}
		});
	}
	
}
