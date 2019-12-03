package com.yy.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yy.core.dao.ad.ContentCategoryDao;
import com.yy.core.pojo.ad.Content;
import com.yy.core.pojo.ad.ContentCategory;
import com.yy.core.pojo.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private ContentCategoryDao contentCategoryDao;

	@Override
	public PageResult search(ContentCategory contentCategory, int page, int rows) {
		PageHelper.startPage(page,rows);
		Page<ContentCategory> page1 = (Page<ContentCategory>)contentCategoryDao.selectByExample(null);
		return new PageResult(page1.getTotal(),page1.getResult());
	}

	@Override
	public void add(ContentCategory contentCategory) {
		contentCategoryDao.insert(contentCategory);
	}

	@Override
	public ContentCategory findOne(Long id) {
		return contentCategoryDao.selectByPrimaryKey(id);
	}

	@Override
	public void update(ContentCategory contentCategory) {
		contentCategoryDao.updateByPrimaryKey(contentCategory);
	}

	@Override
	public void delete(Long[] ids) {
		for (Long id : ids){
			contentCategoryDao.deleteByPrimaryKey(id);
		}
	}

	@Override
	public List<ContentCategory> findAll() {
		return contentCategoryDao.selectByExample(null);
	}
}
