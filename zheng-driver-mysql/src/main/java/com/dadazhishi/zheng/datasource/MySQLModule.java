package com.dadazhishi.zheng.datasource;

import com.google.inject.Provides;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;

public class MySQLModule extends AbstractDataSourceModule {

  public static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
  private DataSourceConfig config;

  public MySQLModule() {
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
    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setUrl(config.getJdbcUrl());
    dataSource.setUser(config.getUsername());
    dataSource.setPassword(config.getPassword());
    return dataSource;
  }
}
