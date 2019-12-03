package com.yy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-redis.xml")
public class SetTest {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void setValue(){
		redisTemplate.boundSetOps("nameset").add("安琪拉");
		redisTemplate.boundSetOps("nameset").add("亚瑟");
		redisTemplate.boundSetOps("nameset").add("张飞");
	}

	@Test
	public void getValue(){
		Set nameset = redisTemplate.boundSetOps("nameset").members();
		System.out.println(nameset);
	}

	@Test
	public void removeValue(){
		redisTemplate.boundSetOps("nameset").remove("张飞");
	}

	@Test
	public void deleteValue(){
		redisTemplate.delete("nameset");
	}
}
