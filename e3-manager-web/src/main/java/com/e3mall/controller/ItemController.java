package com.e3mall.controller;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.EasyUIDataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbItem;
import com.e3mall.service.ItemService;

/**
 * 商品管理的controller
 * @author Dell
 *
 */
@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Resource
	private ItemService service;
	
	@Resource
	private JmsTemplate jmsTemplate;
	
	@Resource
	private Destination topicDestination;
	
	@RequestMapping("/getItem/{itemId}")
	@ResponseBody
	public E3Result getItem(@PathVariable long itemId){
		E3Result result = service.getItemById(itemId);
		return result;
	}
	
	//分页
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDataGridResult list(Integer page, Integer rows){
		EasyUIDataGridResult result = service.list(page, rows);
		return result;
	}
	
	//新增商品
	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result save(TbItem item, String desc, String itemParams) throws Exception{
		//添加商品
		E3Result result = service.addItem(item, desc, itemParams);
		//添加成功，发送信息
		if(result.getStatus()==200){
			final long itemId = (long) result.getData();
			//发送消息
			jmsTemplate.send(topicDestination, new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					//构建消息对象
					TextMessage textMessage = session.createTextMessage(itemId+"");
					//返回消息
					return textMessage;
				}
			});
		}
		return result;
	}
}
