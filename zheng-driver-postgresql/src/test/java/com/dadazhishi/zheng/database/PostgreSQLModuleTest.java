package com.dadazhishi.zheng.database;

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
    DataBaseConfig dataBaseConfig = new DataBaseConfig();
    dataBaseConfig.setDriverClassName(PostgreSQLModule.DRIVER_CLASS_NAME);
    dataBaseConfig.setPassword("postgres");
    dataBaseConfig.setUsername("postgres");
    dataBaseConfig.setUrl("jdbc:postgresql://localhost:" + port + "/postgres");
    PostgreSQLModule postgreSQLModule = new PostgreSQLModule();
    postgreSQLModule.init(dataBaseConfig);
    Connection connection = Guice.createInjector(postgreSQLModule)
        .getInstance(Connection.class);
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("PostgreSQL", metaData.getDatabaseProductName());
  }

  @org.junit.Test
  public void dataSource() throws SQLException {
    DataBaseConfig dataBaseConfig = new DataBaseConfig();
    dataBaseConfig.setDriverClassName(PostgreSQLModule.DRIVER_CLASS_NAME);
    dataBaseConfig.setPassword("postgres");
    dataBaseConfig.setUsername("postgres");
    dataBaseConfig.setUrl("jdbc:postgresql://localhost:" + port + "/postgres");
    PostgreSQLModule postgreSQLModule = new PostgreSQLModule();
    postgreSQLModule.init(dataBaseConfig);
    DataSource dataSource = Guice.createInjector(postgreSQLModule)
        .getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("PostgreSQL", metaData.getDatabaseProductName());
  }
}