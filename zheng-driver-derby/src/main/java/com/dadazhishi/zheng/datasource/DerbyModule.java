package com.dadazhishi.zheng.datasource;

import com.google.inject.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DerbyModule extends AbstractDataSourceModule {

  public static final String DRIVER_CLASS_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
  private DataSourceConfig config;

  public DerbyModule() {
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


}
