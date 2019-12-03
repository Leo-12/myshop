package com.yy.core.service;

import com.yy.core.pojo.entity.GoodsEntiey;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.good.Goods;

public interface GoodsService {
	void add(GoodsEntiey goodsEntiey);

	PageResult findPage(Goods goods, int page, int rows);

	GoodsEntiey findOne(Long id);

	void update(GoodsEntiey goodsEntiey);

	void delete(Long id);

	void updateStatus(Long ids,String status);

	void updateIsMarketable(Long[] ids, String isMarketable);

}
