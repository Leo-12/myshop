package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.item.ItemCat;
import com.yy.core.service.ItemCatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

	@Reference
	private ItemCatService itemCatService;

	@RequestMapping("/findByParentId")
	public List<ItemCat> findByParentId(Long parentId){
		return itemCatService.findByParentId(parentId);
	}

	@RequestMapping("/findOne")
	public ItemCat findItemCat(Long id){
		return itemCatService.findItemCat(id);
	}

	@RequestMapping("/findAll")
	public List<ItemCat> findAll(){
		return itemCatService.findAllGoods();
	}
}
