package com.dadazhishi.zheng.rabbitmq;

import static com.google.inject.name.Names.named;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationObjectMapper;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Singleton;

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
      RabbitMQConfig rabbitMQConfig = ConfigurationObjectMapper
          .resolve(configuration, RabbitMQConfig.NAMESPACE, RabbitMQConfig.class);
      bind(RabbitMQConfig.class).toInstance(rabbitMQConfig);
      bind(ConnectionFactory.class).toProvider(ConnectionFactoryProvider.class).in(Singleton.class);
    } else {
      Map<String, RabbitMQConfig> map = ConfigurationObjectMapper
          .resolveMap(configuration, RabbitMQConfig.NAMESPACE, RabbitMQConfig.class);
      for (Entry<String, RabbitMQConfig> entry : map.entrySet()) {
        String name = entry.getKey();
        RabbitMQConfig rabbitMQConfig = entry.getValue();
        bind(Key.get(RabbitMQConfig.class, named(name))).toInstance(rabbitMQConfig);
        bind(Key.get(ConnectionFactory.class, named(name)))
            .toProvider(new ConnectionFactoryProvider(rabbitMQConfig)).in(Singleton.class);

      }
    }
  }
}
