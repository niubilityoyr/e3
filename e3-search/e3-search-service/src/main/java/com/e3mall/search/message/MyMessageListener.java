package com.e3mall.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 消息监听的测试
 * @author Dell
 *
 */
public class MyMessageListener implements MessageListener{

	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println("接收到信息---"+textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
