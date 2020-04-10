package com.github.zhengframework.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

public class DataSourceProvider implements Provider<DataSource> {

  private HikariConfig config;

  @Inject
  public DataSourceProvider(HikariConfig config) {
    this.config = config;
  }

  @Override
  public DataSource get() {
    return new HikariDataSource(config);
  }
}
