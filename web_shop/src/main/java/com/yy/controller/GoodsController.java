package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.entity.GoodsEntiey;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.Result;
import com.yy.core.pojo.good.Goods;
import com.yy.core.pojo.item.Item;
import com.yy.core.service.GoodsService;
import com.yy.core.service.SolrManagerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	/*@Reference
	private SolrManagerService solrManagerService;*/

	@RequestMapping("/add")
	public Result add(@RequestBody GoodsEntiey goodsEntiey){
		//获取登录名
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goodsEntiey.getGoods().setSellerId(sellerId);
		for (Item item : goodsEntiey.getItemList()){
			item.setCreateTime(new Date());
		}
		try{
			goodsService.add(goodsEntiey);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@RequestMapping("/search")
	public PageResult search(@RequestBody Goods goods, int page, int rows){
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.setSellerId(sellerId);
		return goodsService.findPage(goods,page,rows);
	}

	@RequestMapping("/findOne")
	public GoodsEntiey findOne(Long id){
		return goodsService.findOne(id);
	}

	@RequestMapping("/update")
	public Result update(@RequestBody GoodsEntiey goodsEntiey){
		GoodsEntiey goodsEntiey1 = goodsService.findOne(goodsEntiey.getGoods().getId());
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!goodsEntiey1.getGoods().getSellerId().equals(sellerId) || !goodsEntiey.getGoods().getSellerId().equals(sellerId)){
			return new Result(false,"操作非法");
		}
		try{
			goodsService.update(goodsEntiey);
			return new Result(true,"更新成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"更新失败");
		}
	}

	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try{
			if (ids != null){
				for (Long id : ids){
					goodsService.delete(id);
					//根据商品id删除solr索引库中的数据
					//solrManagerService.deleteItemFormSolr(id);
				}
			}
			return new Result(true,"删除成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
	}

	@RequestMapping("/updateIsMarketable")
	public Result updateIsMarketable(Long[] ids, String isMarketable){
		try{
			goodsService.updateIsMarketable(ids,isMarketable);
			return new Result(true,"上下架成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"上下架失败");
		}
	}
}
