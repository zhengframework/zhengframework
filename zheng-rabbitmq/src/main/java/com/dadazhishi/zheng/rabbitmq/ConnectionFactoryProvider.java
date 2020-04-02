package com.dadazhishi.zheng.rabbitmq;

import com.google.common.base.Strings;
import com.rabbitmq.client.ConnectionFactory;
import javax.inject.Inject;
import javax.inject.Provider;

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

    if (Strings.isNullOrEmpty(rabbitMQConfig.getUri())) {
      factory.setHost(rabbitMQConfig.getHost());
      factory.setVirtualHost(rabbitMQConfig.getVirtualHost());
      factory.setPort(
          ConnectionFactory.portOrDefault(rabbitMQConfig.getPort(), rabbitMQConfig.isSsl()));
      factory.setUsername(rabbitMQConfig.getUsername());
      factory.setPassword(rabbitMQConfig.getPassword());
    } else {
      try {
        factory.setUri(rabbitMQConfig.getUri());
      } catch (Exception e) {
        throw new IllegalArgumentException("uri: " + rabbitMQConfig.getUri());
      }
    }
    return factory;
  }
}
