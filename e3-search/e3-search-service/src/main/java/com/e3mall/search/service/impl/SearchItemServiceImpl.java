package com.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.common.utils.E3Result;
import com.e3mall.search.mapper.ItemMapper;
import com.e3mall.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService{

	@Autowired
	private ItemMapper mapper;
	
	@Autowired
	private SolrServer solrServer;
	
	public E3Result importAllItem() {
		try {
			//从mysql中查询出所有的商品数据
			List<SearchItem> list = mapper.getAllItem();
			//循环添加文档对象
			for (SearchItem item : list) {
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", item.getId());
				document.addField("item_title", item.getTitle());
				document.addField("item_sell_point", item.getSell_point());
				document.addField("item_price", item.getPrice());
				document.addField("item_image", item.getImage());
				document.addField("item_category_name", item.getName());
				solrServer.add(document);
			}
			//提交
			solrServer.commit();
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, "导入数据失败！！！");
		}
	}
	
}
