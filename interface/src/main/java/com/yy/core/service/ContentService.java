package com.yy.core.service;

import com.yy.core.pojo.ad.Content;
import com.yy.core.pojo.entity.PageResult;

import java.util.List;

public interface ContentService {
	PageResult search(Content content, int page, int rows);

	List<Content> findAll();

	void add(Content content);

	Content findOne(Long id);

	void update(Content content);

	void delete(Long[] ids);

	List<Content> findByCategoryId(Long categoryId);

	List<Content> findByCategoryIdFromRedis(Long categoryId);
}
