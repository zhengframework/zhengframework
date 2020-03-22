package com.dadazhishi.zheng.datasource;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HsqldbModuleTest {

  @Before
  public void setup() {

  }

  @After
  public void stop() {

  }

  @Test
  public void connection() throws SQLException {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setJdbcUrl("jdbc:hsqldb:mem:mymemdb");
    dataSourceConfig.setUsername("sa");
    dataSourceConfig.setPassword("");
    HsqldbModule h2Module = new HsqldbModule();
    h2Module.init(dataSourceConfig);
    Connection connection = Guice.createInjector(h2Module).getInstance(Connection.class);
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("HSQL Database Engine", metaData.getDatabaseProductName());
  }
}