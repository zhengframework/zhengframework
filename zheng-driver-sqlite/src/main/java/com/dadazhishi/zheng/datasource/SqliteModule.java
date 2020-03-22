package com.dadazhishi.zheng.datasource;

import com.google.inject.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.sqlite.SQLiteDataSource;

public class SqliteModule extends AbstractDataSourceModule {

  public static final String DRIVER_CLASS_NAME = "org.sqlite.JDBC";
  private DataSourceConfig config;

  public SqliteModule() {
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
    SQLiteDataSource dataSource = new SQLiteDataSource();
    dataSource.setUrl(config.getJdbcUrl());
    return dataSource;
  }
}
