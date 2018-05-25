package com.e3mall.service;

import com.e3mall.common.pojo.EasyUIDataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;

public interface ItemService {
	E3Result getItemById(long itemId);
	
	EasyUIDataGridResult list(Integer page, Integer rows);

	E3Result addItem(TbItem item, String desc, String itemParams);
	
	TbItemDesc getDescByItemId(long itemId);
}
