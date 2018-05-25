package com.e3mall.content.service;

import java.util.List;

import com.e3mall.common.pojo.EasyUITreeNode;
import com.e3mall.common.utils.E3Result;

public interface ContentCategoryService {
	
	public List<EasyUITreeNode> getCategoryList(long parentId);
	
	E3Result insert(long parentId, String name);
}
