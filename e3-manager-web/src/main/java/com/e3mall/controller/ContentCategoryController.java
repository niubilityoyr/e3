package com.e3mall.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.EasyUITreeNode;
import com.e3mall.common.utils.E3Result;
import com.e3mall.content.service.ContentCategoryService;

/**
 * 内容分类管理
 * @author Dell
 *
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	
	@Resource
	private ContentCategoryService categoryService;
	
	//返回tree所需要的数据
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> getlist(@RequestParam(value="id", defaultValue="0") long parentId){
		List<EasyUITreeNode> list = categoryService.getCategoryList(parentId);
		return list;
	}
	
	//新增
	@RequestMapping(value="/create", method=RequestMethod.POST)
	@ResponseBody
	public E3Result create(long parentId, String name){
		E3Result result = categoryService.insert(parentId, name);
		return result;
	}
}
