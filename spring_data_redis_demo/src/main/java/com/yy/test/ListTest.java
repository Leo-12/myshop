package com.yy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-redis.xml")
public class ListTest {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void setValue(){
		redisTemplate.boundListOps("namelist").leftPush("安琪拉");
		redisTemplate.boundListOps("namelist").leftPush("亚瑟");
		redisTemplate.boundListOps("namelist").rightPush("张飞");
	}

	@Test
	public void getValue(){
		List namelist = redisTemplate.boundListOps("namelist").range(0, 10);
		System.out.println(namelist);
	}

	@Test
	public void deleteValue(){
		redisTemplate.delete("namelist");
	}

	@Test
	public void searchByIndex(){
		Object namelist = redisTemplate.boundListOps("namelist").index(1);
		System.out.println(namelist);
	}

	@Test
	public void removeByIndex(){
		redisTemplate.boundListOps("namelist").remove(2,"张飞");
	}


}
