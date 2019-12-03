package com.yy.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.yy.core.dao.user.UserDao;
import com.yy.core.pojo.user.User;
import com.yy.core.service.UserService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author yy
 * @date 2019/11/28 15:35
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ActiveMQQueue activeMQQueue;

	@Autowired
	private UserDao userDao;

	@Value("${template_code}")
	private String template_code;
	@Value("${sign_name}")
	private String sign_name;

	@Override
	public void add(User user) {
		user.setCreated(new Date());
		user.setUpdated(new Date());
		String password = DigestUtils.md5Hex(user.getPassword());//对密码加密
		user.setPassword(password);
		userDao.insert(user);
	}

	@Override
	public Boolean checkSmsCheck(String phone, String smsCode) {
		if (phone == null || smsCode == null || "".equals(phone) || "".equals(smsCode)){
			return false;
		}
		//1、到redis中获取刚才存取的验证码
		String redisSmsCode = (String)redisTemplate.boundValueOps(phone).get();
		//2、判断页面传入的数据和取出来的验证码
		if (smsCode.equals(redisSmsCode)){
			return true;
		}
		return false;
	}

	@Override
	public void sendCode(String phone) {
		//1、生成一个随机的六位数
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 1; i < 7; i ++){
			int n = new Random().nextInt(10);
			stringBuffer.append(n);
		}
		//2、将手机号码为键，验证码为值存到redis中，设置有效时间十分钟
		redisTemplate.boundValueOps(phone).set(stringBuffer.toString(),60*10, TimeUnit.SECONDS);
		final String smsCode = stringBuffer.toString();
		//3、将手机号码 短信内容 模板编号 签名 封装到map 发送给消息服务器
		jmsTemplate.send(activeMQQueue, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = session.createMapMessage();
				message.setString("mobile", phone);
				message.setString("template_code", template_code);
				message.setString("sign_name", sign_name);

				Map map = new HashMap();
				map.put("code",smsCode);
				message.setString("param", JSON.toJSONString(map));
				return message;
			}
		});
	}
}
