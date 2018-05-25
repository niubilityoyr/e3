package com.e3mall.item.controller;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3mall.common.utils.E3Result;
import com.e3mall.item.pojo.Item;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.service.ItemService;

@Controller
public class ItemController {
	
	@Resource
	private ItemService service;
	
	@RequestMapping("/item/{itemId}")
	public String showItem(@PathVariable long itemId, Model model){
		//商品信息
		E3Result result = service.getItemById(itemId);
		TbItem tbItem  = (TbItem) result.getData();
		Item item = new Item();
		BeanUtils.copyProperties(tbItem, item);
		
		//商品详情信息
		TbItemDesc desc = service.getDescByItemId(itemId);
		
		model.addAttribute("itemDesc", desc);
		model.addAttribute("item", item);
		return "item";
	}
	
}
