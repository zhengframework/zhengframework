package com.dadazhishi.zheng.datasource;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import com.opentable.db.postgres.embedded.EmbeddedPostgres.Builder;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import me.alexpanov.net.FreePortFinder;
import org.junit.After;
import org.junit.Before;

public class PostgreSQLModuleTest {

  //  @Rule
//  public SingleInstancePostgresRule pg = EmbeddedPostgresRules.singleInstance();
  EmbeddedPostgres embeddedPostgres;
  int port;

  @Before
  public void setup() throws IOException {
    port = FreePortFinder.findFreeLocalPort();
    Builder builder = EmbeddedPostgres.builder();
    builder.setPort(port);
    embeddedPostgres = builder.start();
  }

  @After
  public void stop() throws IOException {
    embeddedPostgres.close();
  }

  @org.junit.Test
  public void connection() throws SQLException {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setDriverClassName(PostgreSQLModule.DRIVER_CLASS_NAME);
    dataSourceConfig.setPassword("postgres");
    dataSourceConfig.setUsername("postgres");
    dataSourceConfig.setJdbcUrl("jdbc:postgresql://localhost:" + port + "/postgres");
    PostgreSQLModule postgreSQLModule = new PostgreSQLModule();
    postgreSQLModule.init(dataSourceConfig);
    Connection connection = Guice.createInjector(postgreSQLModule)
        .getInstance(Connection.class);
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("PostgreSQL", metaData.getDatabaseProductName());
  }

  @org.junit.Test
  public void dataSource() throws SQLException {
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setDriverClassName(PostgreSQLModule.DRIVER_CLASS_NAME);
    dataSourceConfig.setPassword("postgres");
    dataSourceConfig.setUsername("postgres");
    dataSourceConfig.setJdbcUrl("jdbc:postgresql://localhost:" + port + "/postgres");
    PostgreSQLModule postgreSQLModule = new PostgreSQLModule();
    postgreSQLModule.init(dataSourceConfig);
    DataSource dataSource = Guice.createInjector(postgreSQLModule)
        .getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("PostgreSQL", metaData.getDatabaseProductName());
  }
}