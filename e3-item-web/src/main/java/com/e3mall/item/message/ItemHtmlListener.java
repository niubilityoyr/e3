package com.e3mall.item.message;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.e3mall.common.utils.E3Result;
import com.e3mall.item.pojo.Item;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 商品添加时监听信息，使用 freeMarker 生成静态页面
 * @author Dell
 *
 */
public class ItemHtmlListener implements MessageListener {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${ITEM_PAGE_URL}")
	private String ITEM_PAGE_URL;

	public void onMessage(Message message) {
		//创建模板
		//获取发送过来的消息
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			//查询出商品信息，商品详情信息
			long itemId = Long.parseLong(text);
			E3Result result = itemService.getItemById(itemId);
			TbItem tbItem = (TbItem) result.getData();
			//商品信息
			Item item = new Item();
			BeanUtils.copyProperties(tbItem, item);
			//商品详情信息
			TbItemDesc desc = itemService.getDescByItemId(itemId);
			
			//生成静态界面
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			//加载模板
			Template template = configuration.getTemplate("item.ftl");
			//构建数据集
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", desc);
			//一个输出流
			Writer out = new FileWriter(new File(ITEM_PAGE_URL+itemId+".html"));
			//输出文件
			template.process(data, out);
			//关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
