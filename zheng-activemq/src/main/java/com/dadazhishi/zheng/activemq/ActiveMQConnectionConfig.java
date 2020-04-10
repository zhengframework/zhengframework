package com.github.zhengframework.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

public interface ActiveMQConnectionConfig {

  void config(ActiveMQConnectionFactory connectionFactory);
}
