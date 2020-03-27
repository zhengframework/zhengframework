package com.dadazhishi.zheng.database;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;

public class H2ModuleTest {

  @org.junit.Test
  public void connection() throws SQLException {
    DataBaseConfig dataBaseConfig = new DataBaseConfig();
    dataBaseConfig.setUrl("jdbc:h2:mem:db1");
    dataBaseConfig.setUsername("sa");
    dataBaseConfig.setPassword("");
    H2Module h2Module = new H2Module();
    h2Module.init(dataBaseConfig);
    Connection connection = Guice.createInjector(h2Module).getInstance(Connection.class);
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("H2", metaData.getDatabaseProductName());
  }

  @org.junit.Test
  public void dataSource() throws SQLException {
    DataBaseConfig dataBaseConfig = new DataBaseConfig();
    dataBaseConfig.setUrl("jdbc:h2:mem:db1");
    dataBaseConfig.setUsername("sa");
    dataBaseConfig.setPassword("");
    H2Module h2Module = new H2Module();
    h2Module.init(dataBaseConfig);
    Connection connection = Guice.createInjector(h2Module).getInstance(DataSource.class)
        .getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("H2", metaData.getDatabaseProductName());
  }
}