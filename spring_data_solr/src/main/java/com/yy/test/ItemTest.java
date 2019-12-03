package com.yy.test;

import com.yy.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yy
 * @date 2019/11/21 13:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-solr.xml")
public class ItemTest {

	@Autowired
	private SolrTemplate solrTemplate;

	@Test
	public void insertTest(){
		List<Item> itemList = new ArrayList<>();
		for (int i = 1; i < 100; i ++){
			Item item = new Item();
			item.setId(i + 1L);
			item.setBrand("华为");
			item.setCategory("手机");
			item.setSeller("华为2号专卖店");
			item.setTitle("华为Mate9");
			item.setPrice(new BigDecimal(2000));
			itemList.add(item);
		}

		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	}

	@Test
	public void deleteTest(){
		//solrTemplate.deleteById("1");
		SimpleQuery query = new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
	}

	@Test
	public void searchTest(){
		SimpleQuery query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_title").contains("华为");
		query.addCriteria(criteria);
	}

}
