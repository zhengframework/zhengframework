package com.github.zhengframework.rabbitmq;

/*-
 * #%L
 * zheng-rabbitmq
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
    Map<String, RabbitMQConfig> rabbitMQConfigMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), RabbitMQConfig.class);
    for (Entry<String, RabbitMQConfig> entry : rabbitMQConfigMap.entrySet()) {
      if (entry.getKey().isEmpty()) {
        RabbitMQConfig rabbitMQConfig = entry.getValue();
        bind(RabbitMQConfig.class).toInstance(rabbitMQConfig);
        OptionalBinder.newOptionalBinder(binder(), ConnectionFactory.class)
            .setDefault()
            .toProvider(ConnectionFactoryProvider.class);
        OptionalBinder.newOptionalBinder(binder(), Connection.class)
            .setDefault()
            .toProvider(ConnectionProvider.class);
      } else {
        String name = entry.getKey();
        RabbitMQConfig rabbitMQConfig = entry.getValue();
        bind(Key.get(RabbitMQConfig.class, named(name))).toInstance(rabbitMQConfig);
        ConnectionFactoryProvider connectionFactoryProvider =
            new ConnectionFactoryProvider(rabbitMQConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ConnectionFactory.class, named(name)))
            .setDefault()
            .toProvider(connectionFactoryProvider);
        ConnectionProvider connectionProvider = new ConnectionProvider(connectionFactoryProvider);
        OptionalBinder.newOptionalBinder(binder(), Key.get(Connection.class, named(name)))
            .setDefault()
            .toProvider(connectionProvider);
      }
    }
  }
}
