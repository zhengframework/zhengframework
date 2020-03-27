package com.dadazhishi.zheng.database;

import com.google.inject.Provides;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class CommonsDbcp2PoolModule extends AbstractDataBaseModule {

  public CommonsDbcp2PoolModule() {
    this(null);
  }

  public CommonsDbcp2PoolModule(DataBaseConfig config) {
    super(config);
  }

  @Provides
  public DataSource dataSource(final DataBaseConfig config) {
    DriverManagerConnectionFactory connectionFactory =
        new DriverManagerConnectionFactory(config.getUrl(), config.getUsername(),
            config.getPassword());
    PoolableConnectionFactory poolableConnectionFactory =
        new PoolableConnectionFactory(connectionFactory, null);
    ObjectPool<PoolableConnection> connectionPool =
        new GenericObjectPool<>(poolableConnectionFactory);
    poolableConnectionFactory.setPool(connectionPool);
    return new PoolingDataSource<>(connectionPool);
  }
}
