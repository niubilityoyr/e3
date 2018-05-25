package com.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.EasyUITreeNode;
import com.e3mall.common.utils.E3Result;
import com.e3mall.content.service.ContentCategoryService;
import com.e3mall.mapper.TbContentCategoryMapper;
import com.e3mall.pojo.TbContentCategory;
import com.e3mall.pojo.TbContentCategoryExample;
import com.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容管理的服务层
 * @author Dell
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{
	
	@Autowired
	private TbContentCategoryMapper mapper;
	
	public List<EasyUITreeNode> getCategoryList(long parentId){
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = mapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbContentCategory category : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(category.getId());
			node.setState(category.getIsParent()?"closed":"open");
			node.setText(category.getName());
			resultList.add(node);
		}
		return resultList;
	}

	public E3Result insert(long parentId, String name) {
		//创建pojo
		TbContentCategory category = new TbContentCategory();
		//赋值
		category.setName(name);
		category.setParentId(parentId);
		category.setStatus(1);
		category.setSortOrder(1);
		category.setIsParent(false);
		category.setCreated(new Date());
		category.setUpdated(new Date());
		//添加
		mapper.insert(category);
		TbContentCategory parent = mapper.selectByPrimaryKey(parentId);
		//判断父节点是否为子节点，如果为子节点，修改成父节点
		if(!parent.getIsParent()) {
			parent.setIsParent(true);
			mapper.updateByPrimaryKey(parent);
		}
		return E3Result.ok(category);
	}
	
}
