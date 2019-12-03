package com.yy.core.pojo.entity;

import com.yy.core.pojo.order.OrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author yy
 * @date 2019/12/2 15:04
 */
public class ByerCart implements Serializable {
	private String sellerId;
	private String sellerName;
	private List<OrderItem> orderItemList;

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	@Override
	public String toString() {
		return "ByerCart{" +
				"sellerId='" + sellerId + '\'' +
				", sellerName='" + sellerName + '\'' +
				", orderItemList=" + orderItemList +
				'}';
	}
}
