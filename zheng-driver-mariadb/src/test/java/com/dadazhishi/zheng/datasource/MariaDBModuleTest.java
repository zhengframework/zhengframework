package com.dadazhishi.zheng.datasource;

import static org.junit.Assert.assertEquals;

import ch.vorburger.mariadb4j.junit.MariaDB4jRule;
import com.google.inject.Guice;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Rule;
import org.junit.Test;

public class MariaDBModuleTest {

  @Rule
  public MariaDB4jRule dbRule = new MariaDB4jRule(0);
  int port;
//  DB db;

//  @Before
//  public void setup() throws ManagedProcessException {
//    port = FreePortFinder.findFreeLocalPort(10000);
//    DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
//    configBuilder.setPort(port);
//
//    db = DB.newEmbeddedDB(configBuilder.build());
//  }
//
//  @After
//  public void stop() throws ManagedProcessException {
//    db.stop();
//  }

  @Test
  public void testConnection() throws SQLException {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
//    dataSourceConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:" + port + "/test");
    System.out.println(dbRule.getURL());
    dataSourceConfig.setJdbcUrl(dbRule.getURL());
    dataSourceConfig.setUsername("root");
    dataSourceConfig.setPassword("");
    MariaDBModule mariaDBModule = new MariaDBModule();
    mariaDBModule.init(dataSourceConfig);
    Connection connection = Guice.createInjector(mariaDBModule).getInstance(Connection.class);
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("MariaDB", metaData.getDatabaseProductName());
  }

  @Test
  public void dataSource() throws SQLException {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
//    dataSourceConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:" + port + "/test");
    System.out.println(dbRule.getURL());
    dataSourceConfig.setJdbcUrl(dbRule.getURL());
    dataSourceConfig.setUsername("root");
    dataSourceConfig.setPassword("");
    MariaDBModule mariaDBModule = new MariaDBModule();
    mariaDBModule.init(dataSourceConfig);
    Connection connection = Guice.createInjector(mariaDBModule).getInstance(DataSource.class)
        .getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("MariaDB", metaData.getDatabaseProductName());
  }
}