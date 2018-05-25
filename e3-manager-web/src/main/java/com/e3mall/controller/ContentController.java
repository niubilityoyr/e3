package com.e3mall.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.EasyUIDataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.content.service.ContentService;
import com.e3mall.pojo.TbContent;

/**
 * 内容管理
 * @author Dell
 *
 */
@Controller
@RequestMapping("/content")
public class ContentController {
	
	@Resource
	private ContentService service;
	
	//分页
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDataGridResult list(Integer page, Integer rows, long categoryId){
		EasyUIDataGridResult result = service.list(page, rows, categoryId);
		return result;
	}
	
	//新增
	@RequestMapping("/save")
	@ResponseBody
	public E3Result save(TbContent content){
		E3Result result = service.save(content);
		return result;
	}
	
}
