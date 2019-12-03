package com.yy.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yy.core.dao.ad.ContentDao;
import com.yy.core.pojo.ad.Content;
import com.yy.core.pojo.ad.ContentQuery;
import com.yy.core.pojo.entity.PageResult;
import com.yy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentDao contentDao;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public PageResult search(Content content, int page, int rows) {
		PageHelper.startPage(page,rows);
		Page<Content> page1 = (Page<Content>)contentDao.selectByExample(null);
		return new PageResult(page1.getTotal(),page1.getResult());
	}

	@Override
	public List<Content> findAll() {
		return contentDao.selectByExample(null);
	}

	@Override
	public void add(Content content) {
		contentDao.insert(content);
		redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content.getCategoryId());
	}

	@Override
	public Content findOne(Long id) {
		return contentDao.selectByPrimaryKey(id);
	}

	@Override
	public void update(Content content) {
		Long categoryId = contentDao.selectByPrimaryKey(content.getId()).getCategoryId();
		redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(categoryId);

		contentDao.updateByPrimaryKey(content);

		if (categoryId.longValue() != content.getCategoryId().longValue()){
			redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content.getCategoryId());
		}
	}

	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			Long categoryId = contentDao.selectByPrimaryKey(id).getCategoryId();
			redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(categoryId);
			contentDao.deleteByPrimaryKey(id);
		}
	}

	@Override
	public List<Content> findByCategoryId(Long categoryId) {
		ContentQuery query = new ContentQuery();
		ContentQuery.Criteria criteria = query.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		return contentDao.selectByExample(query);
	}

	@Override
	public List<Content> findByCategoryIdFromRedis(Long categoryId) {
		List<Content> contentList = (List<Content>)redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).get(categoryId);
		if (contentList == null) {
			contentList = findByCategoryId(categoryId);
			redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).put(categoryId,contentList);
		}
		return contentList;
	}
}
