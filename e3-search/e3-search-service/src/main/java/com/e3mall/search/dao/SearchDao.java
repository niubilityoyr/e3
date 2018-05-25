package com.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.common.pojo.SearchResult;

@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult query(SolrQuery query) throws Exception{
		SearchResult result = new SearchResult();
		//执行查询 返回QueryResponse对象
		QueryResponse response = solrServer.query(query);
		//获取 solrDocumentList 对象
		SolrDocumentList solrDocumentList = response.getResults();
		
		List<SearchItem> items = new ArrayList<>();
		//循环构建结果
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem item = new SearchItem();
			String id = (String) solrDocument.getFieldValue("id");
			item.setId(id);
			//获取高亮显示
			String itemName = null;
			Map<String, Map<String, List<String>>> map = response.getHighlighting();
			Map<String, List<String>> map2 = map.get(id);
			List<String> list = map2.get("item_title");
			if(list!=null && list.size()>0){
				itemName = list.get(0);
			}else{
				itemName = (String)solrDocument.getFieldValue("item_title");
			}
			item.setTitle(itemName);
			item.setPrice((long) solrDocument.getFieldValue("item_price"));
			item.setSell_point((String) solrDocument.getFieldValue("item_sell_point"));
			item.setImage((String) solrDocument.getFieldValue("item_image"));
			item.setName((String) solrDocument.getFieldValue("item_category_name"));
			items.add(item);
		}
		
		//获取命中数
		result.setTotal(solrDocumentList.getNumFound());
		result.setItemList(items);
		return result;
	}
	
}
