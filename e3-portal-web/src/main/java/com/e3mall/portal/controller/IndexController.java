package com.e3mall.portal.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3mall.content.service.ContentService;
import com.e3mall.pojo.TbContent;

@Controller
public class IndexController {
	
	@Resource
	private ContentService contentService;
	
	@Value("${AD_CID}")
	private long AD_CID;
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//获取大广告位
		List<TbContent> list = contentService.getListByCid(AD_CID);
		model.addAttribute("ad1List", list);
		return "index";
	}
}
