package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.entity.Result;
import com.yy.core.pojo.item.ItemCat;
import com.yy.core.service.ItemCatService;
import org.springframework.web.bind.annotation.RequestBody;
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

	@RequestMapping("/add")
	public Result addItemCat(@RequestBody ItemCat itemCat){
		try {
			itemCatService.addItemCat(itemCat);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@RequestMapping("/findOne")
	public ItemCat findItemCat(Long id){
		return itemCatService.findItemCat(id);
	}

	@RequestMapping("/update")
	public Result updateItemCat(@RequestBody ItemCat itemCat){
		try {
			itemCatService.updateItemCat(itemCat);
			return new Result(true,"更新成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"更新失败");
		}
	}

	@RequestMapping("/delete")
	public Result deleteItemCat(Long[] ids){
		try {
			itemCatService.deleteItemCat(ids);
			return new Result(true,"删除成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
	}

	@RequestMapping("/findAll")
	public List<ItemCat> findAll(){
		return itemCatService.findAllGoods();
	}
}
