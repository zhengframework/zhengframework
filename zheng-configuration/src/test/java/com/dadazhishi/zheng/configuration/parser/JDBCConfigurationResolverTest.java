package com.dadazhishi.zheng.configuration.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Set;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;

public class JDBCConfigurationResolverTest {

  private JdbcDataSource dataSource;
  private JDBCConfigurationResolver resolver;

  @After
  public void close() throws SQLException {
    Connection connection = dataSource.getConnection();
    Statement statement1 = connection.createStatement();
    statement1.execute("DROP TABLE IF EXISTS config;");
    statement1.close();
  }

  @Before
  public void setup() throws SQLException {
    String sqlKeySet = "select config_key from config";
    String sqlGetKey = "select config_value from config where config_key=?";
    dataSource = new JdbcDataSource();
    dataSource.setUrl("jdbc:h2:mem:db1");
    dataSource.setUser("sa");
    dataSource.setPassword("");
    Connection connection = dataSource.getConnection();

    Statement statement = connection.createStatement();
    statement.execute(
        "create table config(ID INT auto_increment PRIMARY KEY,config_key VARCHAR(255),config_value VARCHAR(255))");
    statement.close();

    for (int i = 0; i < 100; i++) {
      PreparedStatement preparedStatement = connection
          .prepareStatement("insert into config(config_key,config_value) values(?,?)");
      preparedStatement.setString(1, "test" + (i + 1));
      preparedStatement.setString(2, "value" + (i + 1));
      preparedStatement.execute();
    }
    resolver = new JDBCConfigurationResolver(dataSource, sqlKeySet, sqlGetKey);
  }

  @org.junit.Test
  public void get() {
    Optional<String> test = resolver.get("test1");
    System.out.println(test);
    assertTrue(test.isPresent());
    assertEquals("value1", test.get());
  }

  @org.junit.Test
  public void getMissing() {
    Optional<String> test = resolver.get("test1000");
    assertEquals(Optional.empty(), test);
  }


  @org.junit.Test
  public void keySet() {
    Set<String> strings = resolver.keySet();
    assertEquals(100, strings.size());
  }
}