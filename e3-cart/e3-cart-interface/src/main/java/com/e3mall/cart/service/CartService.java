package com.e3mall.cart.service;

import java.util.List;

import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbItem;

public interface CartService {
	
	E3Result addCart(Long userId, long itemId, int num);
	
	List<TbItem> getCartList(Long userId);
	
	E3Result mergeCart(List<TbItem> cartList, Long userId);
	
	E3Result updateNum(Long userId, long itemId, int num);
	
	E3Result deleteCart(Long userId, long itemId);
	
	E3Result emptyCart(Long userId);
}
