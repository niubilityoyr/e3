package com.e3mall.search.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.service.SearchService;

/**
 * 管理搜索
 * @author Dell
 *
 */
@Controller
public class SearchController {
	
	@Resource
	private SearchService service;
	
	@Value("${PAGE_ROWS}")
	private Integer rows;
	
	@RequestMapping("/search")
	public String query(@RequestParam(value="keyword") String key, Model model, 
			@RequestParam(defaultValue="1") Integer page)
					throws Exception{
		//get请求乱码解决
		key = new String(key.getBytes("ISO-8859-1"), "utf-8");
		SearchResult result = service.query(key, page, rows);
		model.addAttribute("query", key);
		model.addAttribute("recourdCount", result.getTotal());
		model.addAttribute("totalPages", result.getPageCount());
		model.addAttribute("itemList", result.getItemList());
		return "search";
	}
	
}
