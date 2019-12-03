package com.yy.listener;

import com.yy.core.service.SolrManagerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author yy
 * @date 2019/11/27 14:42
 */
public class ItemSearchListener implements MessageListener {

	@Autowired
	private SolrManagerService solrManagerService;

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage mqTextMessage = (ActiveMQTextMessage) message;
		try {
			String goodsId = mqTextMessage.getText();
			solrManagerService.saveItemToSolr(Long.parseLong(goodsId));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
