package com.yy.core.service;

import com.yy.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {
	List<ItemCat> findByParentId(Long parentId);

	void addItemCat(ItemCat itemCat);

	ItemCat findItemCat(Long id);

	void updateItemCat(ItemCat itemCat);

	void deleteItemCat(Long[] ids);

	List<ItemCat> findAllGoods();
}
