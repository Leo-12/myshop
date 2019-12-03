package com.yy.core.service;

import java.util.Map;

/**
 * @author yy
 * @date 2019/11/26 14:09
 */
public interface CmsService {
	//取数据
	Map<String,Object> findGoodsData(Long goodsId);
	//根据取到的数据生成页面
	void createStaticPage(Long goodsId,Map<String,Object> rootMap) throws Exception;
}
