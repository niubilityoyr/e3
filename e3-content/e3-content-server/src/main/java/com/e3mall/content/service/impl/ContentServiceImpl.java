package com.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.EasyUIDataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.content.service.ContentService;
import com.e3mall.mapper.TbContentMapper;
import com.e3mall.pojo.TbContent;
import com.e3mall.pojo.TbContentExample;
import com.e3mall.pojo.TbContentExample.Criteria;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper mapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_LIST_KEY}")
	private String CONTENT_LIST_KEY;
	
	public EasyUIDataGridResult list(Integer page, Integer rows, long categoryId) {
		//要返回的数据
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		//设置分页条件
		PageHelper.startPage(page, rows);
		//查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//条件
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = mapper.selectByExample(example);
		PageInfo<TbContent> info = new PageInfo<>(list);
		result.setRows(list);
		result.setTotal(info.getTotal());
		return result;
	}

	public E3Result save(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		mapper.insert(content);
		
		//同步缓存
		jedisClient.hdel(CONTENT_LIST_KEY, content.getCategoryId()+"");
		
		return E3Result.ok();
	}

	public List<TbContent> getListByCid(long cId) {
		//从redis中取数据
		try {
			String json = jedisClient.hget(CONTENT_LIST_KEY, cId+"");
			if(!StringUtils.isBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//从mysql中取数据
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cId);
		List<TbContent> list = mapper.selectByExampleWithBLOBs(example);
		
		//往redis中存数据
		try {
			jedisClient.hset(CONTENT_LIST_KEY, cId+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
