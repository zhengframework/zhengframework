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

import com.google.common.base.Strings;
import com.rabbitmq.client.ConnectionFactory;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ConnectionFactoryProvider implements Provider<ConnectionFactory> {

  private final RabbitMQConfig rabbitMQConfig;

  @Inject
  public ConnectionFactoryProvider(RabbitMQConfig rabbitMQConfig) {
    this.rabbitMQConfig = rabbitMQConfig;
  }

  @Override
  public ConnectionFactory get() {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setConnectionTimeout(rabbitMQConfig.getConnectionTimeout());
    factory.setHandshakeTimeout(rabbitMQConfig.getHandshakeTimeout());
    factory.setShutdownTimeout(rabbitMQConfig.getShutdownTimeout());
    factory.setChannelRpcTimeout(rabbitMQConfig.getChannelRpcTimeout());
    factory.setWorkPoolTimeout(rabbitMQConfig.getWorkPoolTimeout());
    factory.setRequestedChannelMax(rabbitMQConfig.getRequestedChannelMax());
    factory.setRequestedFrameMax(rabbitMQConfig.getRequestedFrameMax());
    factory.setRequestedHeartbeat(rabbitMQConfig.getRequestedHeartbeat());
    factory.setNetworkRecoveryInterval(rabbitMQConfig.getNetworkRecoveryInterval());
    factory.setAutomaticRecoveryEnabled(rabbitMQConfig.isAutomaticRecovery());
    factory.setTopologyRecoveryEnabled(rabbitMQConfig.isTopologyRecovery());

    if (Strings.isNullOrEmpty(rabbitMQConfig.getUrl())) {
      factory.setHost(rabbitMQConfig.getHost());
      factory.setVirtualHost(rabbitMQConfig.getVirtualHost());
      factory.setPort(
          ConnectionFactory.portOrDefault(rabbitMQConfig.getPort(), rabbitMQConfig.isSsl()));
      factory.setUsername(rabbitMQConfig.getUsername());
      factory.setPassword(rabbitMQConfig.getPassword());
    } else {
      try {
        factory.setUri(rabbitMQConfig.getUrl());
      } catch (Exception e) {
        throw new IllegalArgumentException("url: " + rabbitMQConfig.getUrl());
      }
    }
    return factory;
  }
}
