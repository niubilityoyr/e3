package com.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.utils.E3Result;
import com.e3mall.order.pojo.OrderInfo;
import com.e3mall.order.service.OrderService;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;

/**
 * 管理订单的controller
 * @author Dell
 *
 */
@Controller
public class OrderController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	//进入确认订单界面
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request, Model model){
		//获取登录的用户
		TbUser user = (TbUser) request.getAttribute("user");
		//获取要显示的购物车列表
		List<TbItem> list = cartService.getCartList(user.getId());
		model.addAttribute("cartList", list);
		//返回逻辑视图
		return "order-cart";
	}
	
	//提交订单
	@RequestMapping("/order/create")
	public String create(OrderInfo orderInfo, HttpServletRequest request, Model model){
		//获取登录的用户，补全用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//把订单信息存入mysql中
		E3Result result = orderService.create(orderInfo);
		if(result.getStatus()==200){
			//清空购物车信息
			cartService.emptyCart(user.getId());
		}
		model.addAttribute("orderId", result.getData());
		model.addAttribute("payment", orderInfo.getPayment());
		return "success";
	}
	
}
