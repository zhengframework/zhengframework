package com.dadazhishi.zheng.datasource;

import com.google.inject.Provides;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import javax.sql.DataSource;

/**
 * https://github.com/swaldman/c3p0 https://www.mchange.com/projects/c3p0/#using_c3p0
 */
public class C3P0Module extends AbstractDataSourceModule {

  private DataSourceConfig config;

  @Override
  public void init(DataSourceConfig config) {
    this.config = config;
  }

  @Provides
  public DataSource dataSource() {
    ComboPooledDataSource cpds = new ComboPooledDataSource();
    try {
      cpds.setDriverClass(config.getDriverClassName());
    } catch (PropertyVetoException e) {
      throw new RuntimeException(e);
    }
    cpds.setJdbcUrl(config.getJdbcUrl());
    cpds.setUser(config.getUsername());
    cpds.setPassword(config.getPassword());
    cpds.setProperties(config.getDataSourceProperties());
    return cpds;
  }
}
