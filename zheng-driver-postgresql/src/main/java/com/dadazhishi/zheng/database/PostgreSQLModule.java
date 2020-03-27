package com.dadazhishi.zheng.database;

import com.google.inject.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLModule extends AbstractDataBaseModule {

  public static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";

  public PostgreSQLModule() {
    this(null);
  }

  public PostgreSQLModule(DataBaseConfig config) {
    super(config);
    try {
      Class.forName(DRIVER_CLASS_NAME);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Provides
  public Connection connection(final DataBaseConfig config) {
    try {
      return DriverManager
          .getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
