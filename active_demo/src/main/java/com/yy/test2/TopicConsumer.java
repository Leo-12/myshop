package com.yy.test2;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author yy
 * @date 2019/11/27 10:26
 */
public class TopicConsumer {
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
		Topic topic = session.createTopic("test-topic");
		//6、创建消息的接受者
		MessageConsumer consumer = session.createConsumer(topic);
		//7、创建监听器接收消息
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println("接收消息" + textMessage.getText());
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		//8、等待键盘输入
		System.in.read();
		//9、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
