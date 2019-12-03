package com.yy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yy
 * @date 2019/11/28 9:23
 */
@RestController
@RequestMapping("/testJms")
public class TestJms {

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@RequestMapping("/send")
	public void send(String text){
		jmsMessagingTemplate.convertAndSend("java",text);
	}

	@RequestMapping("/sendsms")
	public void sendMap(){
		Map map = new HashMap();
		map.put("mobile", "13225596099");
		map.put("template_code", "SMS_178980158");
		map.put("sign_name", "品优购");
		map.put("param", "{\"code\":\"123456\"}");
		jmsMessagingTemplate.convertAndSend("sms",map);
	}
}
