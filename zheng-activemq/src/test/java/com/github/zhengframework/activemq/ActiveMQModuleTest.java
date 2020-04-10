package com.github.zhengframework.activemq;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Rule;
import org.junit.Test;

public class ActiveMQModuleTest {


  @Rule
  public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();


  @Test
  public void test() throws JMSException {
//    ActiveMQConnectionFactory connectionFactory2 = new ActiveMQConnectionFactory("vm://embedded-broker?create=false");

    ConnectionFactory connectionFactory = Guice
        .createInjector(new ActiveMQModule("vm://embedded-broker?create=false"))
        .getInstance(ConnectionFactory.class);
    Connection connection = connectionFactory.createConnection();
    ConnectionMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getJMSVersion());
    System.out.println(metaData.getJMSProviderName());
    System.out.println(metaData.getProviderVersion());
    assertEquals("1.1", metaData.getJMSVersion());
    assertEquals("ActiveMQ", metaData.getJMSProviderName());

    connection.close();
  }
}