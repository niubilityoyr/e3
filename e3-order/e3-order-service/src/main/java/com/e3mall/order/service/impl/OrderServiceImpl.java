package com.e3mall.order.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.utils.E3Result;
import com.e3mall.mapper.TbOrderItemMapper;
import com.e3mall.mapper.TbOrderMapper;
import com.e3mall.mapper.TbOrderShippingMapper;
import com.e3mall.order.pojo.OrderInfo;
import com.e3mall.order.service.OrderService;
import com.e3mall.pojo.TbOrderItem;
import com.e3mall.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper shippingMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ORDER_ID_KEY}")
	private String ORDER_ID_KEY;
	
	@Value("${ORDER_ID_INIT_VALUE}")
	private String ORDER_ID_INIT_VALUE;
	
	@Value("${ORDER_ITEM_ID_KEY}")
	private String ORDER_ITEM_ID_KEY;
	
	public E3Result create(OrderInfo orderInfo) {
		//订单id，不能重复，是串数字，不能太长
		Boolean exists = jedisClient.exists(ORDER_ID_KEY);
		if(!exists){
			//订单id在redis中第一次存在时
			jedisClient.set(ORDER_ID_KEY, ORDER_ID_INIT_VALUE);
		}
		Long orderId = jedisClient.incr(ORDER_ID_KEY);
		//补全信息
		orderInfo.setOrderId(orderId.toString());
		//状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		Date date = new Date();
		orderInfo.setCreateTime(date);
		orderInfo.setUpdateTime(date);
		//保存订单
		orderMapper.insert(orderInfo);
		
		//保存订单商品详细信息
		for (TbOrderItem orderItem : orderInfo.getOrderItems()) {
			//补全信息
			Long orderItemId = jedisClient.incr(ORDER_ITEM_ID_KEY);
			orderItem.setId(orderItemId.toString());
			orderItem.setOrderId(orderId.toString());
			orderItemMapper.insert(orderItem);
		}
		
		//保存订单收货地址信息
		TbOrderShipping shipping = orderInfo.getOrderShipping();
		shipping.setCreated(date);
		shipping.setOrderId(orderId.toString());
		shippingMapper.insert(shipping);
		
		return E3Result.ok(orderId);
	}

}
