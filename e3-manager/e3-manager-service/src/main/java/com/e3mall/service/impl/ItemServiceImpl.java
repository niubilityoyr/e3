package com.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.EasyUIDataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.IDUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbItemDescMapper;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.pojo.TbItemExample;
import com.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper mapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	
	@Value("${REDS_ITEM_DETE}")
	private Integer REDS_ITEM_DETE;

	public EasyUIDataGridResult list(Integer page, Integer rows) {
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		//分页条件
		PageHelper.startPage(page, rows);
		//开始查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = mapper.selectByExample(example);
		result.setRows(list);
		//获取总记录数
		PageInfo<TbItem> info = new PageInfo<>(list);
		result.setTotal(info.getTotal());
		return result;
	}

	public E3Result addItem(TbItem item, String desc, String itemParams) {
		//补全信息
		Long id = IDUtils.genItemId();
		item.setId(id);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		
		//添加商品
		mapper.insert(item);
		
		//添加商品描述
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDesc.setItemId(id);
		itemDesc.setItemDesc(desc);
		itemDescMapper.insert(itemDesc);
		
		return E3Result.ok(id);
	}
	
	public E3Result getItemById(long itemId) {
		//从redis中取
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY+":"+itemId+":INFO");
			if(!StringUtils.isBlank(json)){
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return E3Result.ok(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//从mysql中取
		TbItem item = mapper.selectByPrimaryKey(itemId);
		
		//往redis中存
		try {
			jedisClient.set(REDIS_ITEM_KEY+":"+itemId+":INFO", JsonUtils.objectToJson(item));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_KEY+":"+itemId+":INFO", REDS_ITEM_DETE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok(item);
	}
	
	public TbItemDesc getDescByItemId(long itemId) {
		//从redis中取
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY+":"+itemId+":DESC");
			if(!StringUtils.isBlank(json)){
				TbItemDesc desc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return desc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//从mysql中取
		TbItemDesc desc = itemDescMapper.selectByPrimaryKey(itemId);
		
		//往redis中存
		try {
			jedisClient.set(REDIS_ITEM_KEY+":"+itemId+":DESC", JsonUtils.objectToJson(desc));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_KEY+":"+itemId+":DESC", REDS_ITEM_DETE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return desc;
	}
}
