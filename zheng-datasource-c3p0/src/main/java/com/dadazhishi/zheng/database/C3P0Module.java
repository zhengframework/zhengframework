package com.dadazhishi.zheng.database;

import com.google.inject.Provides;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;
import javax.sql.DataSource;

/**
 * https://github.com/swaldman/c3p0 https://www.mchange.com/projects/c3p0/#using_c3p0
 */
public class C3P0Module extends AbstractDataBaseModule {

  public C3P0Module() {
    this(null);
  }

  public C3P0Module(DataBaseConfig config) {
    super(config);
  }

  @Provides
  public DataSource dataSource(final DataBaseConfig config) {
    ComboPooledDataSource cpds = new ComboPooledDataSource();
    try {
      cpds.setDriverClass(config.getDriverClassName());
    } catch (PropertyVetoException e) {
      throw new RuntimeException(e);
    }
    cpds.setJdbcUrl(config.getUrl());
    cpds.setUser(config.getUsername());
    cpds.setPassword(config.getPassword());
    Properties properties = new Properties();
    properties.putAll(config.getProperties());
    cpds.setProperties(properties);
    return cpds;
  }
}
