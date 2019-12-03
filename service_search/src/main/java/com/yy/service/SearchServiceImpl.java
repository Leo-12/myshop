package com.yy.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yy.core.pojo.item.Item;
import com.yy.core.service.SearchService;
import com.yy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yy
 * @date 2019/11/21 16:03
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SolrTemplate solrTemplate;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public Map<String, Object> search(Map paramMap) {
		//1、根据关键字到solr中查询(分页)总条数、总页数
		Map<String, Object> resultMap = highlightSearch(paramMap);
		//2、根据查询的参数
		List<String> groupCatgroupList = findGroupCatgroupList(paramMap);
		resultMap.put("categoryList",groupCatgroupList);

		//3、判断paramMap传入的参数中是否有分类的名称
		String category = String.valueOf(paramMap.get("category"));
		if (category != null && !"".equals(category)){
			//如果有分类参数，则根据分类查询对应的品牌集合
			Map specListAndBrandList = findSpecListAndBrandList(category);
			resultMap.putAll(specListAndBrandList);
		}else{
			//如果没有，根据第一个分类查询对应的商品集合
			Map specListAndBrandList = findSpecListAndBrandList(groupCatgroupList.get(0));
			resultMap.putAll(specListAndBrandList);
		}
		return resultMap;
	}

	private Map<String, Object> highlightSearch(Map paramMap){
		//获取关键字
		String keywords = String.valueOf(paramMap.get("keywords"));
		if (keywords != null){
			keywords = keywords.replaceAll(" ", "");
		}
		Integer pageNo = (Integer)paramMap.get("pageNo");
		Integer pageSize = (Integer)paramMap.get("pageSize");
		//封装查询的对象
		HighlightQuery query = new SimpleHighlightQuery();

		//按照分类筛选
		if(!"".equals(paramMap.get("category"))){
			Criteria filterCriteria=new Criteria("item_category").is(paramMap.get("category"));
			FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}

		//品牌筛选
		if(!"".equals(paramMap.get("brand"))){
			Criteria filterCriteria=new Criteria("item_brand").is(paramMap.get("brand"));
			FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}

		//规格筛选
		if(paramMap.get("spec")!=null){
			Map<String,String> specMap= (Map)paramMap.get("spec");
			for(String key:specMap.keySet() ){
				Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key) );
				FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}

		//按价格筛选
		if(!"".equals(paramMap.get("price"))){
			String[] price = ((String)paramMap.get("price")).split("-");

			if(!price[0].equals("0")){//如果区间起点不等于0
				Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(price[0]);
				FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);
			}

			if(!price[1].equals("*")){//如果区间终点不等于*
				Criteria filterCriteria=new  Criteria("item_price").lessThanEqual(price[1]);
				FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}

		//排序
		String sortValue= (String)paramMap.get("sort");//ASC  DESC
		String sortField= (String)paramMap.get("sortField");//排序字段

		if(sortValue!=null && !sortValue.equals("")){
			if(sortValue.equals("ASC")){
				Sort sort=new Sort(Sort.Direction.ASC, "item_"+sortField);
				query.addSort(sort);
			}
			if(sortValue.equals("DESC")){
				Sort sort=new Sort(Sort.Direction.DESC, "item_"+sortField);
				query.addSort(sort);
			}
		}

		//查询的条件对象
		Criteria criteria = new Criteria("item_keywords").is(keywords);
		//将查询条件放入对象中
		query.addCriteria(criteria);
		//计算从第几条开始查询
		if (pageNo == null || pageNo <= 0){
			pageNo = 1;
		}
		//当前页-1
		Integer start = (pageNo - 1) * pageSize;
		//设置第几条开始
		query.setOffset(start);
		//每页查询多少条数据
		query.setRows(pageSize);

		//创建高亮显示对象
		HighlightOptions options = new HighlightOptions();
		//设置哪个域需要高亮显示
		options.addField("item_title");
		//高亮的前缀
		options.setSimplePrefix("<em style=\"color:red\">");
		//高亮的后缀
		options.setSimplePostfix("</em>");
		//将高亮添加到查询对象中
		query.setHighlightOptions(options);
		//查询并返回结果
		HighlightPage<Item> items = solrTemplate.queryForHighlightPage(query, Item.class);
		//获取带高亮的集合
		List<HighlightEntry<Item>> highlighted = items.getHighlighted();
		List<Item> list = new ArrayList<>();
		for (HighlightEntry<Item> entry : highlighted){
			Item item = entry.getEntity();
			List<HighlightEntry.Highlight> highlights = entry.getHighlights();
			if (highlights != null && highlights.size() > 0){
				//获取高亮的标题集合
				List<String> highlightTitle = highlights.get(0).getSnipplets();
				if (highlightTitle != null && highlightTitle.size() > 0){
					//获取高亮的标题
					String title = highlightTitle.get(0);
					item.setTitle(title);
				}
			}
			list.add(item);
		}
		Map<String, Object> resultMap = new HashMap<>();
		//查询到的结果集
		resultMap.put("rows",list);
		//总页数
		resultMap.put("totalPages",items.getTotalPages());
		//总条数
		resultMap.put("total",items.getTotalElements());
		return resultMap;
	}
	//2、根据查询的参数，到solr中获取对应的分类结果，按分组方式去重
	private List<String> findGroupCatgroupList(Map paramMap){
		List<String> resultList = new ArrayList<>();
		//获取关键字
		String keywords = String.valueOf(paramMap.get("keywords"));
		if (keywords != null){
			keywords = keywords.replaceAll(" ","");
		}
		//创建查询对象
		SimpleQuery query = new SimpleQuery();
		//创建查询条件对象
		Criteria criteria = new Criteria("item_keywords").is(keywords);
		//将查询条件放入到查询对象中
		query.addCriteria(criteria);

		//创建分组对象
		GroupOptions groupOptions = new GroupOptions();
		//设置根据分类域进行分组
		groupOptions.addGroupByField("item_category");
		//将分组对象放入查询对象中
		query.setGroupOptions(groupOptions);
		//使用分组查询分类集合
		GroupPage<Item> items = solrTemplate.queryForGroupPage(query, Item.class);
		//获得结果集合中的分类域集合
		GroupResult<Item> item_category = items.getGroupResult("item_category");
		//获取分类域中的实体集合
		Page<GroupEntry<Item>> groupEntries = item_category.getGroupEntries();
		//遍历实体集合得到实体对象
		for (GroupEntry<Item> groupEntry : groupEntries){
			String groupValue = groupEntry.getGroupValue();
			//组装到集合中
			resultList.add(groupValue);
		}
		return resultList;
	}
	//4、根据分类名称查询对应商品集合和规格集合
	private Map findSpecListAndBrandList(String categoryName){
		//a、根据分类名称到redis中查询对应的模板id
		Long templateId = (Long)redisTemplate.boundHashOps(Constants.CATECOPY_LIST_REDIS).get(categoryName);
		//b、根据模板id到redis中查询对应的品牌集合
		List<Map> brandList = (List<Map>)redisTemplate.boundHashOps(Constants.BRAND_LIST_REDIS).get(templateId);
		//c、根据模板id到redis中查询对应的规格集合
		List<Map> specList = (List<Map>)redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS).get(templateId);
		//d、将品牌集合和规格集合封装到Map中
		Map resultMap = new HashMap();
		resultMap.put("brandList",brandList);
		resultMap.put("specList",specList);
		return resultMap;
	}
}
