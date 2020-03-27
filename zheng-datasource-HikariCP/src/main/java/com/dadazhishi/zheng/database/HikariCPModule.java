package com.dadazhishi.zheng.database;

import com.google.inject.Provides;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;

/**
 * https://github.com/brettwooldridge/HikariCP
 */
public class HikariCPModule extends AbstractDataBaseModule {

  public HikariCPModule() {
    this(null);
  }

  public HikariCPModule(DataBaseConfig config) {
    super(config);
  }

  @Provides
  public DataSource dataSource(final DataBaseConfig config) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(config.getDriverClassName());
    hikariConfig.setUsername(config.getUsername());
    hikariConfig.setPassword(config.getPassword());
    hikariConfig.setJdbcUrl(config.getUrl());
    Properties properties = new Properties();
    properties.putAll(config.getProperties());
    hikariConfig.setDataSourceProperties(properties);
    return new HikariDataSource(hikariConfig);
  }

}
