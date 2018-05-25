package com.e3mall.search.solr;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJTest {

	@Test
	public void add() throws Exception{
		//创建连接 solrServer
		SolrServer solrServer = new HttpSolrServer("http://192.168.100.21:8080/solr");
		//创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		//添加域
		document.addField("id", "oyr123");
		document.addField("item_title", "测试商品1");
		document.addField("item_price", 123456);
		//添加
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void delete() throws Exception{
		//创建连接 solrServer
		SolrServer solrServer = new HttpSolrServer("http://192.168.100.21:8080/solr");
		//根据id删除
		//solrServer.deleteById("oyr123");
		//根据条件删除
		solrServer.deleteByQuery("id:oyr123");
		solrServer.commit();
	}
	
	//简单查询
	@Test
	public void select() throws Exception{
		//创建连接 solrServer
		SolrServer solrServer = new HttpSolrServer("http://192.168.100.21:8080/solr");
		//创建查询对象
		SolrQuery query = new SolrQuery();
		//设置主查询条件
		query.setQuery("*:*");
		//执行查询 返回QueryResponse对象
		QueryResponse response = solrServer.query(query);
		//获取 solrDocumentList 对象
		SolrDocumentList solrDocumentList = response.getResults();
		//获取命中数
		System.out.println("总命中数为："+solrDocumentList.getNumFound());
		
		//循环出结果集
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.getFieldValue("id"));
			System.out.println(solrDocument.getFieldValue("item_title"));
			//solrDocument.getFieldValue("item_sell_point");
			//solrDocument.getFieldValue("item_price");
			//solrDocument.getFieldValue("item_image");
			//solrDocument.getFieldValue("item_category_name");
		}
	}
	
	//复杂查询
	@Test
	public void select2() throws Exception{
		//创建连接 solrServer
		SolrServer solrServer = new HttpSolrServer("http://192.168.100.21:8080/solr");
		//创建查询对象
		SolrQuery query = new SolrQuery();
		//主查询条件
		query.setQuery("手机");
		//分页
		query.setStart(0);
		query.setRows(10);
		//默认搜索域
		query.set("df", "item_keywords");
		//开启高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePost("</em>");
		query.setHighlightSimplePre("<em>");
		
		//执行查询 返回QueryResponse对象
		QueryResponse response = solrServer.query(query);
		//获取 solrDocumentList 对象
		SolrDocumentList solrDocumentList = response.getResults();
		//获取命中数
		System.out.println("总命中数为："+solrDocumentList.getNumFound());
		
		//循环出结果集
		for (SolrDocument solrDocument : solrDocumentList) {
			String id = (String) solrDocument.getFieldValue("id");
			System.out.println(id);
			//System.out.println(solrDocument.getFieldValue("item_title"));
			Map<String, Map<String, List<String>>> map = response.getHighlighting();
			Map<String, List<String>> map2 = map.get(id);
			List<String> list = map2.get("item_title");
			if(list!=null && list.size()>0){
				String itemName = list.get(0);
				System.out.println(itemName);
			}
			//;
			//;
			//;
			//;
		}
	}
	
}
