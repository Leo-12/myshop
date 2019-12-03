package com.yy.core.service;

import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.seller.Seller;

public interface SellerService {
	void addSeller(Seller seller);

	PageResult searchSeller(Seller seller, int page, int rows);

	Seller findSeller(String sellerId);

	void updateStatus(String sellerId, String status);
}
