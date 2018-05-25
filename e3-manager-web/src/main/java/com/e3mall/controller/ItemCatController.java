package com.e3mall.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.EasyUITreeNode;
import com.e3mall.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Resource
	public ItemCatService service;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> list(@RequestParam(value="id", defaultValue="0") long parentId){
		List<EasyUITreeNode> result = service.getByParent(parentId);
		return result;
	}
	
}
