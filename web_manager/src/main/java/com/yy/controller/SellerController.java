package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.Result;
import com.yy.core.pojo.seller.Seller;
import com.yy.core.service.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;

	@RequestMapping("/search")
	public PageResult searchSeller(@RequestBody Seller seller, int page, int rows){
		PageResult pageResult = sellerService.searchSeller(seller, page, rows);
		return pageResult;
	}

	@RequestMapping("/findOne")
	public Seller findSeller(String sellerId){
		return sellerService.findSeller(sellerId);
	}

	@RequestMapping("/updateStatus")
	public Result updateStatus(String sellerId, String status){
		try {
			sellerService.updateStatus(sellerId,status);
			return new Result(true,"更新成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"更新失败");
		}
	}
}
