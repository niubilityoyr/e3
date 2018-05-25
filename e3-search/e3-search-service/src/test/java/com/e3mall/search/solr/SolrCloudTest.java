package com.e3mall.search.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrCloudTest {
	
	//集群版添加
	@Test
	public void SolrCloudAdd() throws Exception{
		//创建连接
		CloudSolrServer solrServer = new CloudSolrServer("192.168.100.21:2181,192.168.100.21:2182,192.168.100.21:2183");
		//设置默认的Collection
		solrServer.setDefaultCollection("collection2");
		//创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		//设置域
		document.setField("id", "oyr123");
		document.setField("item_title", "欧阳荣专属商品");
		//添加
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	
	//集群版查询
	@Test
	public void SolrCloudQuery() throws Exception{
		//连接
		CloudSolrServer solrServer = new CloudSolrServer("192.168.100.21:2181,192.168.100.21:2182,192.168.100.21:2183");
		//设置默认的Collection
		solrServer.setDefaultCollection("collection2");
		
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		
		QueryResponse response = solrServer.query(query);
		SolrDocumentList solrDocumentList = response.getResults();
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.getFieldValue("id"));
		}
	}
	
}
