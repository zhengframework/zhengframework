package com.dadazhishi.zheng.rabbitmq;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractIdleService;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@SuppressWarnings("ALL")
@Singleton
public class ConnectionProvider extends AbstractIdleService implements Provider<Connection> {

  private final Provider<ConnectionFactory> connectionFactoryProvider;

  private Connection connection;

  @Inject
  public ConnectionProvider(
      Provider<ConnectionFactory> connectionFactoryProvider) {
    this.connectionFactoryProvider = connectionFactoryProvider;
  }

  @Override
  public Connection get() {
    return connection;
  }

  @Override
  protected void startUp() throws Exception {
    Preconditions.checkState(connection == null, "connection is not null");
    connection = connectionFactoryProvider.get().newConnection();
  }

  @Override
  protected void shutDown() throws Exception {
    connection.close();
  }
}
