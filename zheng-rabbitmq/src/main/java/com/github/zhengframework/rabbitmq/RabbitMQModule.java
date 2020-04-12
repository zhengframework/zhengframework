package com.github.zhengframework.rabbitmq;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Map;
import java.util.Map.Entry;

public class RabbitMQModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, RabbitMQConfig> rabbitMQConfigMap = ConfigurationBeanMapper
        .resolve(configuration, RabbitMQConfig.class);
    for (Entry<String, RabbitMQConfig> entry : rabbitMQConfigMap.entrySet()) {
      if(entry.getKey().isEmpty()){
        RabbitMQConfig rabbitMQConfig=entry.getValue();
        bind(RabbitMQConfig.class).toInstance(rabbitMQConfig);
        OptionalBinder.newOptionalBinder(binder(),ConnectionFactory.class)
            .setDefault().toProvider(ConnectionFactoryProvider.class);
        OptionalBinder.newOptionalBinder(binder(),Connection.class)
            .setDefault().toProvider(ConnectionProvider.class);
      }else {
        String name = entry.getKey();
        RabbitMQConfig rabbitMQConfig = entry.getValue();
        bind(Key.get(RabbitMQConfig.class, named(name))).toInstance(rabbitMQConfig);
        ConnectionFactoryProvider connectionFactoryProvider = new ConnectionFactoryProvider(
            rabbitMQConfig);
        OptionalBinder.newOptionalBinder(binder(),Key.get(ConnectionFactory.class, named(name)))
        .setDefault().toProvider(connectionFactoryProvider);
        ConnectionProvider connectionProvider = new ConnectionProvider(connectionFactoryProvider);
        OptionalBinder.newOptionalBinder(binder(),Key.get(Connection.class, named(name)))
            .setDefault().toProvider(connectionProvider);
      }
    }
  }
}
