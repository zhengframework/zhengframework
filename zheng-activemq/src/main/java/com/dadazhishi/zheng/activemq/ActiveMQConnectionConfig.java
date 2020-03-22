package com.dadazhishi.zheng.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

public interface ActiveMQConnectionConfig {

  void config(ActiveMQConnectionFactory connectionFactory);
}
