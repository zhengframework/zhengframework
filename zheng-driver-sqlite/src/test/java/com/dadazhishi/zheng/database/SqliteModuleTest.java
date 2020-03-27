package com.dadazhishi.zheng.database;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class SqliteModuleTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @org.junit.Test
  public void connection() throws SQLException, IOException {
    DataBaseConfig dataBaseConfig = new DataBaseConfig();
    File file = folder.newFile("test.db");
    dataBaseConfig.setUrl("jdbc:sqlite:" + file.getCanonicalPath());
    SqliteModule sqliteModule = new SqliteModule();
    sqliteModule.init(dataBaseConfig);
    Connection connection = Guice.createInjector(sqliteModule).getInstance(Connection.class);
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("SQLite", metaData.getDatabaseProductName());
  }

  @org.junit.Test
  public void dataSource() throws SQLException, IOException {
    DataBaseConfig dataBaseConfig = new DataBaseConfig();
    File file = folder.newFile("test.db");
    dataBaseConfig.setUrl("jdbc:sqlite:" + file.getCanonicalPath());
    SqliteModule sqliteModule = new SqliteModule();
    sqliteModule.init(dataBaseConfig);
    Connection connection = Guice.createInjector(sqliteModule).getInstance(DataSource.class)
        .getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    System.out.println(metaData.getDatabaseProductVersion());
    assertEquals("SQLite", metaData.getDatabaseProductName());
  }
}