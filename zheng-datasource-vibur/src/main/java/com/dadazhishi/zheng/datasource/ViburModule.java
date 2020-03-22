package com.dadazhishi.zheng.datasource;

import com.google.inject.Provides;
import javax.sql.DataSource;
import org.vibur.dbcp.ViburDBCPDataSource;

public class ViburModule extends AbstractDataSourceModule {

  private DataSourceConfig config;

  @Override
  public void init(DataSourceConfig config) {
    this.config = config;
  }

  @Provides
  public DataSource dataSource() {
    ViburDBCPDataSource ds = new ViburDBCPDataSource();
    ds.setDriverClassName(config.getDriverClassName());
    ds.setJdbcUrl(config.getJdbcUrl());
    ds.setUsername(config.getUsername());
    ds.setPassword(config.getPassword());
    ds.setDriverProperties(config.getDataSourceProperties());
    return ds;
  }
}
