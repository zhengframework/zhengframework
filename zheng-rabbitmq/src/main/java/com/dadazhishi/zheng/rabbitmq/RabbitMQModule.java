package com.dadazhishi.zheng.rabbitmq;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.rabbitmq.client.ConnectionFactory;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class RabbitMQModule extends AbstractModule {

  private String uri;

  public RabbitMQModule(String uri) {
    this.uri = uri;
  }

  @Override
  protected void configure() {
  }

  @Provides
  public ConnectionFactory connectionFactory() {
    ConnectionFactory factory = new ConnectionFactory();
    try {
      factory.setUri(uri);
//      factory.setUri("amqp://userName:password@hostName:portNumber/virtualHost");
      return factory;
    } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }
}
