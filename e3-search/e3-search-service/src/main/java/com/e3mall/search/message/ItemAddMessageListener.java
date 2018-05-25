package com.e3mall.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.search.mapper.ItemMapper;

/**
 * 监听商品的添加
 * @author Dell
 *
 */
public class ItemAddMessageListener implements MessageListener{

	@Autowired
	private ItemMapper mapper;
	
	@Autowired
	private SolrServer solrServer;
	
	//消息监听
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			//获取发送过来的消息
			String text = textMessage.getText();
			long itemId = Long.parseLong(text);
			//查询结果
			SearchItem item = mapper.getById(itemId);
			//添加到索引库
			SolrInputDocument document = new SolrInputDocument();
			//添加域
			document.addField("id", item.getId());
			document.addField("item_title", item.getTitle());
			document.addField("item_sell_point", item.getSell_point());
			document.addField("item_price", item.getPrice());
			document.addField("item_image", item.getImage());
			document.addField("item_category_name", item.getName());
			//添加
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
