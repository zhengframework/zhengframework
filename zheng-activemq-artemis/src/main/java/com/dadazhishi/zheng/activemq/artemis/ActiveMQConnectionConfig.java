package com.dadazhishi.zheng.activemq.artemis;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public interface ActiveMQConnectionConfig {

  void config(ActiveMQConnectionFactory connectionFactory);

}
