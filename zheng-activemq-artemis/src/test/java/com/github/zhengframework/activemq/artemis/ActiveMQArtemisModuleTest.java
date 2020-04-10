package com.github.zhengframework.activemq.artemis;

import static org.junit.Assert.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.multibindings.OptionalBinder;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.junit.Rule;
import org.junit.Test;

public class ActiveMQArtemisModuleTest {

  final static ActiveMQConnectionConfig activeMQConnectionConfig = new ActiveMQConnectionConfig() {
    @Override
    public void config(ActiveMQConnectionFactory connectionFactory) {
      connectionFactory.setClientID("aaa");
    }
  };
  @Rule
  public EmbeddedActiveMQResource resource = new EmbeddedActiveMQResource();

  @Test
  public void connectionFactory() throws JMSException {
    ConnectionFactory connectionFactory = Guice
        .createInjector(new ActiveMQArtemisModule("vm://0"),
            new AbstractModule() {
              @Override
              protected void configure() {

                OptionalBinder.newOptionalBinder(binder(), ActiveMQConnectionConfig.class)
                    .setBinding()
                    .toInstance(activeMQConnectionConfig)
                ;
              }
            }
        )
        .getInstance(ConnectionFactory.class);
    ActiveMQConnectionFactory activeMQConnectionFactory = (ActiveMQConnectionFactory) connectionFactory;
    System.out.println(activeMQConnectionFactory.getClientID());
    assertEquals("aaa", activeMQConnectionFactory.getClientID());
    Connection connection = connectionFactory.createConnection();
    ConnectionMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getJMSVersion());
    System.out.println(metaData.getJMSProviderName());
    System.out.println(metaData.getProviderVersion());
    assertEquals("2.0", metaData.getJMSVersion());
    assertEquals("ActiveMQ", metaData.getJMSProviderName());

    connection.close();
  }
}