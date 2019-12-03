package com.yy.util;

import com.alibaba.fastjson.JSON;
import com.yy.core.dao.item.ItemDao;
import com.yy.core.pojo.item.Item;
import com.yy.core.pojo.item.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author yy
 * @date 2019/11/21 14:51
 */
@Component
public class DataImportToSolr {

	@Autowired
	private SolrTemplate solrTemplate;

	@Autowired
	private ItemDao itemDao;

	private void importItemDataToSolr(){
		ItemQuery query = new ItemQuery();
		ItemQuery.Criteria criteria = query.createCriteria();
		criteria.andStatusEqualTo("1");
		List<Item> itemList = itemDao.selectByExample(query);
		if (itemList != null){
			for (Item item : itemList){
				String specJson = item.getSpec();
				Map map = JSON.parseObject(specJson, Map.class);
				item.setSpecMap(map);
			}
			solrTemplate.saveBeans(itemList);
			solrTemplate.commit();
		}
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		DataImportToSolr bean = (DataImportToSolr)context.getBean("dataImportToSolr");
		bean.importItemDataToSolr();
	}
}
