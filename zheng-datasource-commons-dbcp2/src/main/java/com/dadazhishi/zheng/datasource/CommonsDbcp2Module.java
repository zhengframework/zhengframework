package com.dadazhishi.zheng.datasource;

import com.google.inject.Provides;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class CommonsDbcp2Module extends AbstractDataSourceModule {

  private DataSourceConfig config;

  @Override
  public void init(DataSourceConfig config) {
    this.config = config;
  }

  @Provides
  public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(config.getDriverClassName());
    dataSource.setUrl(config.getJdbcUrl());
    dataSource.setUsername(config.getUsername());
    dataSource.setPassword(config.getPassword());
    Properties properties = config.getDataSourceProperties();
    if (properties != null) {
      for (String propertyName : properties.stringPropertyNames()) {
        dataSource.addConnectionProperty(propertyName, properties.getProperty(propertyName));
      }
    }
    return dataSource;
  }
}
