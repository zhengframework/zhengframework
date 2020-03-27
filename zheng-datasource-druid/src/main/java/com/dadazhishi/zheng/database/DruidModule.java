package com.dadazhishi.zheng.database;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.inject.Provides;
import java.util.Properties;
import javax.sql.DataSource;

public class DruidModule extends AbstractDataBaseModule {

  public DruidModule() {
    this(null);
  }

  public DruidModule(DataBaseConfig config) {
    super(config);
  }

  @Provides
  public DataSource dataSource(final DataBaseConfig config) {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName(config.getDriverClassName());
    dataSource.setUrl(config.getUrl());
    dataSource.setUsername(config.getUsername());
    dataSource.setPassword(config.getPassword());
    Properties properties = new Properties();
    properties.putAll(config.getProperties());
    dataSource.setConnectProperties(properties);
    return dataSource;
  }

}
