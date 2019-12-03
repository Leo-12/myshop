package com.yy.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yy.core.dao.item.ItemCatDao;
import com.yy.core.pojo.item.ItemCat;
import com.yy.core.pojo.item.ItemCatQuery;
import com.yy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ItemCatServiceImp implements ItemCatService {

	@Autowired
	private ItemCatDao itemCatDao;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<ItemCat> findByParentId(Long parentId) {
		//获取到所有的分类数据
		List<ItemCat> itemCatsList = itemCatDao.selectByExample(null);
		//分类名称作为key模板的id作为值
		for (ItemCat itemCat : itemCatsList){
			redisTemplate.boundHashOps(Constants.CATECOPY_LIST_REDIS).put(itemCat.getName(),itemCat.getTypeId());
		}

		//根据父级ID查询子集，展示到页面
		ItemCatQuery query = new ItemCatQuery();
		ItemCatQuery.Criteria criteria = query.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		return itemCatDao.selectByExample(query);
	}

	@Override
	public void addItemCat(ItemCat itemCat) {
		itemCatDao.insertSelective(itemCat);
	}

	@Override
	public ItemCat findItemCat(Long id) {
		return itemCatDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateItemCat(ItemCat itemCat) {
		itemCatDao.updateByPrimaryKeySelective(itemCat);
	}

	@Override
	public void deleteItemCat(Long[] ids) {
		for (Long id : ids){
			itemCatDao.deleteByPrimaryKey(id);
		}
	}

	@Override
	public List<ItemCat> findAllGoods() {
		return itemCatDao.selectByExample(null);
	}
}
