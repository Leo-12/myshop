package com.yy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yy
 * @date 2019/11/28 9:12
 */
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		//启动当前项目中Tomcat插件
		SpringApplication.run(Application.class,args);
	}
}
