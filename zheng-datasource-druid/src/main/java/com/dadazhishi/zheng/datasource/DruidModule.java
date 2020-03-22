package com.dadazhishi.zheng.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.inject.Provides;
import javax.sql.DataSource;

public class DruidModule extends AbstractDataSourceModule {

  private DataSourceConfig config;

  @Override
  public void init(DataSourceConfig config) {
    this.config = config;
  }

  @Provides
  public DataSource dataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName(config.getDriverClassName());
    dataSource.setUrl(config.getJdbcUrl());
    dataSource.setUsername(config.getUsername());
    dataSource.setPassword(config.getPassword());
    dataSource.setConnectProperties(config.getDataSourceProperties());
    return dataSource;
  }

}
