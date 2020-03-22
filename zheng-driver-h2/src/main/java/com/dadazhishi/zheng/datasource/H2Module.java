package com.dadazhishi.zheng.datasource;

import com.google.inject.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

public class H2Module extends AbstractDataSourceModule {

  public static final String DRIVER_CLASS_NAME = "org.h2.Driver";
  private DataSourceConfig config;

  public H2Module() {
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
    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setUrl(config.getJdbcUrl());
    dataSource.setUser(config.getUsername());
    dataSource.setPassword(config.getPassword());
    return dataSource;
  }
}
