package com.e3mall.item.pojo;

import org.apache.commons.lang3.StringUtils;

import com.e3mall.pojo.TbItem;

public class Item extends TbItem{
	
	public String[] getImages(){
		if(!StringUtils.isBlank(getImage())){
			return getImage().split(",");
		}
		return null;
	}
	
}
