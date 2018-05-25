package com.e3mall.content.service;

import java.util.List;

import com.e3mall.common.pojo.EasyUIDataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbContent;

public interface ContentService {

	EasyUIDataGridResult list(Integer page, Integer rows, long categoryId);

	E3Result save(TbContent content);

	List<TbContent> getListByCid(long cId);
}
