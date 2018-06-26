package com.hao.shiro.mapper;

import com.hao.shiro.po.ItemsCustom;
import com.hao.shiro.po.ItemsQueryVo;

import java.util.List;


public interface ItemsMapperCustom {

	// 商品查询列表
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo)
			throws Exception;
}
