package com.yy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yy
 * @date 2019/11/28 9:14
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private Environment environment;

	@RequestMapping("/hello")
	public String hello(){
		String url = environment.getProperty("url");
		return "HelloWorld!" + url;
	}
}
