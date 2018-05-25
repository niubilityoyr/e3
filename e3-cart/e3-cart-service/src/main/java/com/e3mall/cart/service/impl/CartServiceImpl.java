package com.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private TbItemMapper itemMapper;
	
	@Value("${CART_LIST_PRE}")
	private String CART_LIST_PRE;
	
	//添加购物车到 redis 中
	public E3Result addCart(Long userId, long itemId, int num) {
		//判断 redis 中是否有此购物车信息
		Boolean b = jedisClient.hexists(CART_LIST_PRE + userId.toString(), itemId+"");
		if(b){
			//购物车已经存在当前商品
			String json = jedisClient.hget(CART_LIST_PRE + userId.toString(), itemId+"");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			//写回 redis 中
			jedisClient.hset(CART_LIST_PRE + userId.toString(), itemId+"", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		//购物车不存在当前商品信息，查询出商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//设置数量，和图片
		item.setNum(num);
		item.setImage(item.getImage().split(",")[0]);
		//保存到 redis 中，存放类型为hash name key value
		jedisClient.hset(CART_LIST_PRE + userId.toString(), itemId+"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	//返回购物车列表
	public List<TbItem> getCartList(Long userId) {
		//从 redis 中取出当前用户的所有购物车信息
		List<String> list = jedisClient.hvals(CART_LIST_PRE + userId.toString());
		List<TbItem> cartList = new ArrayList<>();
		//构建出购物车列表返回
		for (String json : list) {
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			cartList.add(item);
		}
		return cartList;
	}
	
	//合并购物车列表
	public E3Result mergeCart(List<TbItem> cartList, Long userId) {
		for (TbItem tbItem : cartList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}

	//修改购物车数量
	public E3Result updateNum(Long userId, long itemId, int num) {
		//从 redis 中取数据
		String json = jedisClient.hget(CART_LIST_PRE + userId.toString(), itemId+"");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		//修改num
		item.setNum(num);
		//往 redis 回写数据
		jedisClient.hset(CART_LIST_PRE + userId.toString(), itemId+"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	//删除购物车
	public E3Result deleteCart(Long userId, long itemId) {
		//从redis中删除购物车
		jedisClient.hdel(CART_LIST_PRE + userId.toString(), itemId+"");
		return E3Result.ok();
	}

	//清空当前购物车
	public E3Result emptyCart(Long userId) {
		jedisClient.del(CART_LIST_PRE + userId.toString());
		return E3Result.ok();
	}
}
