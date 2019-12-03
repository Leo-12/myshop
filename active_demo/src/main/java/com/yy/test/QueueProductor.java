package com.yy.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author yy
 * @date 2019/11/27 9:53
 * 点对点方式  发送方
 */
public class QueueProductor {
	public static void main(String[] args) throws Exception {
		//1、创建连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.200.128:61616");
		//2、获取连接
		Connection connection = connectionFactory.createConnection();
		//3、启动连接
		connection.start();
		//4、获取session  第一个参数:是否启动事务   第二个参数:消息的确认模式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、创建队列对象，指定发送队列的名称
		Queue queue = session.createQueue("test-qq");
		//6、创建消息的生产者
		MessageProducer producer = session.createProducer(queue);
		//7、创建消息
		TextMessage textMessage = session.createTextMessage("HelloWorld!");
		//8、发布消息
		producer.send(textMessage);
		//9、关闭资源
		producer.close();
		session.close();
		connection.close();
	}
}
