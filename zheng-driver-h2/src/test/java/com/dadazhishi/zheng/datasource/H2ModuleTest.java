package com.dadazhishi.zheng.datasource;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;

public class H2ModuleTest {

  @org.junit.Test
  public void connection() throws SQLException {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setJdbcUrl("jdbc:h2:mem:db1");
    dataSourceConfig.setUsername("sa");
    dataSourceConfig.setPassword("");
    H2Module h2Module = new H2Module();
    h2Module.init(dataSourceConfig);
    Connection connection = Guice.createInjector(h2Module).getInstance(Connection.class);
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("H2", metaData.getDatabaseProductName());
  }

  @org.junit.Test
  public void dataSource() throws SQLException {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setJdbcUrl("jdbc:h2:mem:db1");
    dataSourceConfig.setUsername("sa");
    dataSourceConfig.setPassword("");
    H2Module h2Module = new H2Module();
    h2Module.init(dataSourceConfig);
    Connection connection = Guice.createInjector(h2Module).getInstance(DataSource.class)
        .getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("H2", metaData.getDatabaseProductName());
  }
}