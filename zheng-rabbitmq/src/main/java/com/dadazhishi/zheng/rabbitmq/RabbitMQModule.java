package com.dadazhishi.zheng.rabbitmq;

import static com.google.inject.name.Names.named;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Map;
import java.util.Map.Entry;

public class RabbitMQModule extends AbstractModule implements ConfigurationSupport {

  private Configuration configuration;

  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }


  @Override
  protected void configure() {
    Boolean group = configuration.getBoolean("zheng.rabbitmq.group", false);
    if (!group) {
      RabbitMQConfig rabbitMQConfig = ConfigurationBeanMapper
          .resolve(configuration, RabbitMQConfig.PREFIX, RabbitMQConfig.class);
      bind(RabbitMQConfig.class).toInstance(rabbitMQConfig);
      bind(ConnectionFactory.class).toProvider(ConnectionFactoryProvider.class);
      bind(Connection.class).toProvider(ConnectionProvider.class);
    } else {
      Map<String, RabbitMQConfig> map = ConfigurationBeanMapper
          .resolveMap(configuration, RabbitMQConfig.PREFIX, RabbitMQConfig.class);
      for (Entry<String, RabbitMQConfig> entry : map.entrySet()) {
        String name = entry.getKey();
        RabbitMQConfig rabbitMQConfig = entry.getValue();
        bind(Key.get(RabbitMQConfig.class, named(name))).toInstance(rabbitMQConfig);
        ConnectionFactoryProvider connectionFactoryProvider = new ConnectionFactoryProvider(
            rabbitMQConfig);
        bind(Key.get(ConnectionFactory.class, named(name)))
            .toProvider(connectionFactoryProvider);
        ConnectionProvider connectionProvider = new ConnectionProvider(connectionFactoryProvider);
        bind(Key.get(Connection.class, named(name)))
            .toProvider(connectionProvider);
//        Connection connection = connectionProvider.get();
//        try {
//          Channel channel = connection.createChannel();
//          channel.exchangeDeclare("exchangeName", BuiltinExchangeType.DIRECT.getType());
//          channel.queueDeclare("queueName", true, false, false, null);
//          channel.queueBind("queueName","exchangeName","routeKey",null);
//          channel.basicPublish("exchangeName","routeKey",null,"".getBytes());
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
      }
    }
  }
}
