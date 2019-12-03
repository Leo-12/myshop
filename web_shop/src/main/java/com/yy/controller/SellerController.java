package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.entity.Result;
import com.yy.core.pojo.seller.Seller;
import com.yy.core.service.SellerService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;

	@RequestMapping("/add")
	public Result addSeller(@RequestBody Seller seller){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode(seller.getPassword());
		seller.setPassword(password);
		try{
			sellerService.addSeller(seller);
			return new Result(true,"注册成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"注册失败");
		}
	}
}
