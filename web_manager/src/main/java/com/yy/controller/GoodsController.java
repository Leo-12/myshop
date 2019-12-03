package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.entity.GoodsEntiey;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.Result;
import com.yy.core.pojo.good.Goods;
import com.yy.core.service.CmsService;
import com.yy.core.service.GoodsService;
import com.yy.core.service.SearchService;
import com.yy.core.service.SolrManagerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	@Reference
	private SearchService searchService;

	/*@Reference
	private CmsService cmsService;

	@Reference
	private SolrManagerService solrManagerService;*/

	@RequestMapping("/search")
	public PageResult search(@RequestBody Goods goods, int page, int rows){
		return goodsService.findPage(goods,page,rows);
	}

	@RequestMapping("/findOne")
	public GoodsEntiey findOne(Long id){
		return goodsService.findOne(id);
	}

	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids,String status){

		try {
			if (ids != null){
				for (Long id : ids){
					//1、根据商品的id改变商品的商家的状态
					goodsService.updateStatus(id,status);
					//2、根据商品的id到solr索引库中删除对应的数据
					/*if ("1".equals(status)){
						//3、根据商品id获取库存数据，放入solr索引库中，提供给搜索使用
						solrManagerService.saveItemToSolr(id);
						//4、根据商品的id获取商品的详情数据，并且根据商品详情数据和模板生成页面
						Map<String,Object> goodsData = cmsService.findGoodsData(id);
						cmsService.createStaticPage(id,goodsData);
					}*/
				}
			}
			return new Result(true,"修改状态成功");
		} catch (Exception e){
			e.printStackTrace();
			return new Result(false,"修改状态失败");
		}
	}

	/*@RequestMapping("/testPage")
	public Boolean testCreatePage(Long goodsId){
		try {
			Map<String, Object> goodsData = cmsService.findGoodsData(goodsId);
			cmsService.createStaticPage(goodsId,goodsData);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			if (ids != null){
				for (Long id : ids){
					goodsService.delete(id);
					//solrManagerService.deleteItemFormSolr(id);
				}
			}
			return new Result(true,"删除成功");
		} catch (Exception e){
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
	}

	@RequestMapping("/update")
	public Result update(@RequestBody GoodsEntiey goodsEntity){
		try{
			// 获取当前的登录名
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			// 商品的所有者
			String sellerId = goodsEntity.getGoods().getSellerId();
			if(!userName.equals(sellerId)){
				return new Result(false,"您没有权限修改此商品");
			}
			goodsService.update(goodsEntity);
			return new Result(true,"修改成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"修改失败");
		}
	}
}
