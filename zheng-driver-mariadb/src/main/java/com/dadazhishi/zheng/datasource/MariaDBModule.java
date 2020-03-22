package com.dadazhishi.zheng.datasource;

import com.google.inject.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.mariadb.jdbc.MariaDbDataSource;

public class MariaDBModule extends AbstractDataSourceModule {

  public static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
  private DataSourceConfig config;

  public MariaDBModule() {
    try {
      Class.forName(DRIVER_CLASS_NAME);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void init(DataSourceConfig config) {
    this.config = config;
  }

  @Provides
  public Connection connection() {
    try {
      return DriverManager
          .getConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Provides
  public DataSource dataSource() {
    MariaDbDataSource dataSource = new MariaDbDataSource();
    try {
      dataSource.setUrl(config.getJdbcUrl());
      dataSource.setUserName(config.getUsername());
      dataSource.setPassword(config.getPassword());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return dataSource;
  }
}
