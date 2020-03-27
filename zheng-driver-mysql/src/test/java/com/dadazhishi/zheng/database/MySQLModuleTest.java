package com.dadazhishi.zheng.database;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.distribution.Version;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MySQLModuleTest {

  EmbeddedMysql embeddedMysql;

  @Before
  public void setup() {
    embeddedMysql = EmbeddedMysql.anEmbeddedMysql(Version.v8_latest)
        .start();
  }

  @After
  public void stop() {
    embeddedMysql.stop();
  }

  @Test
  public void connection() throws SQLException {
    MysqldConfig mysqldConfig = embeddedMysql.getConfig();
    DataBaseConfig dataBaseConfig = new DataBaseConfig();
    dataBaseConfig.setUrl("jdbc:mysql://127.0.0.1:" + mysqldConfig.getPort() + "/");
    dataBaseConfig.setUsername(mysqldConfig.getUsername());
    dataBaseConfig.setPassword(mysqldConfig.getPassword());
    MySQLModule mySQLModule = new MySQLModule();
    mySQLModule.init(dataBaseConfig);
    Connection connection = Guice.createInjector(mySQLModule).getInstance(Connection.class);
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("MySQL", metaData.getDatabaseProductName());
  }

  @Test
  public void dataSource() throws SQLException {
    MysqldConfig mysqldConfig = embeddedMysql.getConfig();
    DataBaseConfig dataBaseConfig = new DataBaseConfig();
    dataBaseConfig.setUrl("jdbc:mysql://127.0.0.1:" + mysqldConfig.getPort() + "/");
    dataBaseConfig.setUsername(mysqldConfig.getUsername());
    dataBaseConfig.setPassword(mysqldConfig.getPassword());
    MySQLModule mySQLModule = new MySQLModule();
    mySQLModule.init(dataBaseConfig);
    Connection connection = Guice.createInjector(mySQLModule).getInstance(DataSource.class)
        .getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("MySQL", metaData.getDatabaseProductName());
  }
}