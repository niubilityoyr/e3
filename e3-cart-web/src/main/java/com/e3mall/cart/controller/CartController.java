package com.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;
import com.e3mall.service.ItemService;

/**
 * 管理购物车的controller
 * @author Dell
 *
 */
@Controller
public class CartController {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CartService cartService;
	
	@Value("${COOKIE_CART_DATE}")
	private int COOKIE_CART_DATE;
	
	//添加购物车，返回逻辑视图
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable long itemId, @RequestParam(defaultValue="1") int num, 
			HttpServletRequest request, HttpServletResponse response){
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			//用户登录了，调用服务层
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		//用户没有登录
		//获取购物车列表
		List<TbItem> list = getCartFromCookie(request);
		//判断购物车列表中是否有此商品
		boolean b = true;
		for (TbItem tbItem : list) {
			if(tbItem.getId().equals(itemId)){
				b = false;
				tbItem.setNum(tbItem.getNum()+num);
				break;
			}
		}
		//购物车列表中没有此商品，获取此商品信息，设置数量，添加到购物车
		if(b){
			E3Result result = itemService.getItemById(itemId);
			if(result.getStatus()==200){
				TbItem item = (TbItem) result.getData();
				item.setNum(num);
				item.setImage(item.getImage().split(",")[0]);
				list.add(item);
			}
		}
		//把购物车重新写入到 cookie 中
		CookieUtils.setCookie(request, response, "item_cart", JsonUtils.objectToJson(list), COOKIE_CART_DATE, true);
		return "cartSuccess";
	}
	
	//显示购物车列表，返回逻辑视图
	@RequestMapping("/cart/cart")
	public String cart(HttpServletRequest request, HttpServletResponse response,
			Model model) {
		//从 cookie 中获取购物车列表
		List<TbItem> list = getCartFromCookie(request);
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){//用户登录了
			//合并购物车列表
			cartService.mergeCart(list, user.getId());
			//将 cookie 中的购物车列表删除。
			CookieUtils.deleteCookie(request, response, "item_cart");
			//获取购物车列表
			list = cartService.getCartList(user.getId());
		}
		//保存到域中
		model.addAttribute("cartList", list);
		return "cart";
	}
	
	//修改数量
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateNum(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable long itemId, @PathVariable int num){
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){//用户登录了
			//调用服务层
			return cartService.updateNum(user.getId(), itemId, num);
		}
		//获取购物车列表
		List<TbItem> list = getCartFromCookie(request);
		//修改数量
		for (TbItem tbItem : list) {
			if(tbItem.getId().equals(itemId)){
				tbItem.setNum(num);
			}
		}
		//保存到 cookie 中
		CookieUtils.setCookie(request, response, "item_cart", JsonUtils.objectToJson(list), COOKIE_CART_DATE, true);
		return E3Result.ok();
	}
	
	//删除购物车，返回逻辑视图
	@RequestMapping("/cart/delete/{itemId}")
	public String delete(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable long itemId){
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){//用户登录了
			//调用服务层删除购物车
			cartService.deleteCart(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		//用户没有登录
		//获取购物车列表
		List<TbItem> list = getCartFromCookie(request);
		//删除购物车
		for (TbItem tbItem : list) {
			if(tbItem.getId().equals(itemId)){
				list.remove(tbItem);
				break;
			}
		}
		//保存到 cookie 中
		CookieUtils.setCookie(request, response, "item_cart", JsonUtils.objectToJson(list), COOKIE_CART_DATE, true);
		return "redirect:/cart/cart.html";
	}
	
	//从 cookie 中取购物车列表
	public List<TbItem> getCartFromCookie(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, "item_cart", true);
		if(StringUtils.isBlank(json)){
			return new ArrayList<TbItem>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
}
