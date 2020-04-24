package com.github.zhengframework.rabbitmq;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Map;
import java.util.Map.Entry;

public class RabbitMQModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, RabbitMQConfig> rabbitMQConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), RabbitMQConfig.class);
    for (Entry<String, RabbitMQConfig> entry : rabbitMQConfigMap.entrySet()) {
      if (entry.getKey().isEmpty()) {
        RabbitMQConfig rabbitMQConfig = entry.getValue();
        bind(RabbitMQConfig.class).toInstance(rabbitMQConfig);
        OptionalBinder.newOptionalBinder(binder(), ConnectionFactory.class)
            .setDefault().toProvider(ConnectionFactoryProvider.class);
        OptionalBinder.newOptionalBinder(binder(), Connection.class)
            .setDefault().toProvider(ConnectionProvider.class);
      } else {
        String name = entry.getKey();
        RabbitMQConfig rabbitMQConfig = entry.getValue();
        bind(Key.get(RabbitMQConfig.class, named(name))).toInstance(rabbitMQConfig);
        ConnectionFactoryProvider connectionFactoryProvider = new ConnectionFactoryProvider(
            rabbitMQConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ConnectionFactory.class, named(name)))
            .setDefault().toProvider(connectionFactoryProvider);
        ConnectionProvider connectionProvider = new ConnectionProvider(connectionFactoryProvider);
        OptionalBinder.newOptionalBinder(binder(), Key.get(Connection.class, named(name)))
            .setDefault().toProvider(connectionProvider);
      }
    }
  }
}
