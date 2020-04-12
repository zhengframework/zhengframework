package com.github.zhengframework.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

//@Singleton
public class ConnectionProvider implements Provider<Connection> {

  private Provider<ConnectionFactory> connectionFactoryProvider;

  @Inject
  public ConnectionProvider(
      Provider<ConnectionFactory> connectionFactoryProvider) {

    this.connectionFactoryProvider = connectionFactoryProvider;
  }

  @Override
  public Connection get() {
    try {
      return connectionFactoryProvider.get().newConnection();
    } catch (IOException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

}
