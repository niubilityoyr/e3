package com.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.dao.SearchDao;
import com.e3mall.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchDao searchDao;
	
	//查询
	public SearchResult query(String key, Integer page, Integer rows) throws Exception {
		//构建条件查询对象
		SolrQuery query = new SolrQuery();
		//主查询条件
		query.setQuery(key);
		//分页
		if(page==null || page<=0){
			page = 1;
		}
		query.setStart((page-1)*rows);
		query.setRows(rows);
		//默认搜索域
		query.set("df", "item_keywords");
		//开启高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePost("</font>");
		query.setHighlightSimplePre("<font color='red'>");
		//去dao查询
		SearchResult result = searchDao.query(query);
		result.setPage(page);
		Integer pageCount = (int) (result.getTotal()/rows);
		pageCount = result.getTotal()%rows==0?pageCount:pageCount+1;
		result.setPageCount(pageCount);
		//返回结果
		return result;
	}

}
