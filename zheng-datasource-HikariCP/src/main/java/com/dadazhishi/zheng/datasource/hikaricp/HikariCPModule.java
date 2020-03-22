package com.dadazhishi.zheng.datasource.hikaricp;

import com.dadazhishi.zheng.datasource.AbstractDataSourceModule;
import com.dadazhishi.zheng.datasource.DataSourceConfig;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

/**
 * https://github.com/brettwooldridge/HikariCP
 */
public class HikariCPModule extends AbstractDataSourceModule {

  private HikariConfig hikariConfig;

  protected void configure() {
  }

  @Provides
  public DataSource dataSource() {
    return new HikariDataSource(hikariConfig);
  }

  @Override
  public void init(DataSourceConfig config) {
    hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(config.getDriverClassName());
    hikariConfig.setUsername(config.getUsername());
    hikariConfig.setPassword(config.getPassword());
    hikariConfig.setJdbcUrl(config.getJdbcUrl());
    hikariConfig.setDataSourceProperties(config.getDataSourceProperties());
  }
}
