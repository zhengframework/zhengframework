package com.dadazhishi.zheng.datasource;

import com.google.inject.Provides;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class CommonsDbcp2PoolModule extends AbstractDataSourceModule {

  private DataSourceConfig config;

  @Override
  public void init(DataSourceConfig config) {
    this.config = config;
  }

  @Provides
  public DataSource dataSource() {
    DriverManagerConnectionFactory connectionFactory =
        new DriverManagerConnectionFactory(config.getJdbcUrl(), config.getUsername(),
            config.getPassword());
    PoolableConnectionFactory poolableConnectionFactory =
        new PoolableConnectionFactory(connectionFactory, null);
    ObjectPool<PoolableConnection> connectionPool =
        new GenericObjectPool<>(poolableConnectionFactory);
    poolableConnectionFactory.setPool(connectionPool);
    return new PoolingDataSource<>(connectionPool);
  }
}
