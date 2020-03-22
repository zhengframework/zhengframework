package com.dadazhishi.zheng.activemq;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public interface ActiveMQConnectionConfig {

  void config(ActiveMQConnectionFactory connectionFactory);

}
