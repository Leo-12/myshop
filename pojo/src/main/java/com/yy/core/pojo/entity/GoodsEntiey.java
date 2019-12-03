package com.yy.core.pojo.entity;

import com.yy.core.pojo.good.Goods;
import com.yy.core.pojo.good.GoodsDesc;
import com.yy.core.pojo.item.Item;

import java.io.Serializable;
import java.util.List;

public class GoodsEntiey implements Serializable {

	private Goods goods;
	private GoodsDesc goodsDesc;
	private List<Item> itemList;

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public GoodsDesc getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(GoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	@Override
	public String toString() {
		return "GoodsEntiey{" +
				"goods=" + goods +
				", goodsDesc=" + goodsDesc +
				", itemList=" + itemList +
				'}';
	}
}
