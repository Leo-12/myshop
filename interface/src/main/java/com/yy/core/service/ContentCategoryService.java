package com.yy.core.service;

import com.yy.core.pojo.ad.Content;
import com.yy.core.pojo.ad.ContentCategory;
import com.yy.core.pojo.entity.PageResult;

import java.util.List;

public interface ContentCategoryService {
	PageResult search(ContentCategory contentCategory, int page, int rows);

	void add(ContentCategory contentCategory);

	ContentCategory findOne(Long id);

	void update(ContentCategory contentCategory);

	void delete(Long[] ids);

	List<ContentCategory> findAll();
}
