package com.yy.core.service;

/**
 * @author yy
 * @date 2019/11/26 16:09
 */
public interface SolrManagerService {
	void saveItemToSolr(Long id);

	void deleteItemFormSolr(Long id);
}
