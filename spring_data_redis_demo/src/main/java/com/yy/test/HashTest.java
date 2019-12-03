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
public class HashTest {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void setValue(){
		redisTemplate.boundHashOps("namehash").put("a","one");
		redisTemplate.boundHashOps("namehash").put("b","two");
		redisTemplate.boundHashOps("namehash").put("c","three");
	}

	@Test
	public void getKeys(){
		Object keys = redisTemplate.boundHashOps("namehash").keys();
		System.out.println(keys);
	}

	@Test
	public void getValue(){
		List values = redisTemplate.boundHashOps("namehash").values();
		System.out.println(values);
	}

	@Test
	public void searchValueBykey(){
		Object a = redisTemplate.boundHashOps("namehash").get("a");
		System.out.println(a);
	}

	@Test
	public void deleteValueByKey(){
		redisTemplate.boundHashOps("namehash").delete("c");
	}
}
