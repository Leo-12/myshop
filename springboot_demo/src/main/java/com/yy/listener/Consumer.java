package com.yy.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yy
 * @date 2019/11/28 9:27
 */
@Component
public class Consumer {
	//定义监听器，监听消息服务发送过来的消息
	@JmsListener(destination = "java")
	public void readMessage(String text){
		System.out.println("接收消息" + text);
	}

	@JmsListener(destination = "map")
	public void readMap(Map map){
		System.out.println("接收map" + map);
	}
}
