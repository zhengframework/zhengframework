package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.resolver.ConfigurationResolver;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import javax.sql.DataSource;

public class JDBCConfigurationResolver implements ConfigurationResolver {

  private final String sqlKeySet;
  private final String sqlGetKey;
  private DataSource dataSource;

  public JDBCConfigurationResolver(DataSource dataSource, String sqlKeySet,
      String sqlGetKey) {
    this.dataSource = dataSource;
    this.sqlKeySet = sqlKeySet;
    this.sqlGetKey = sqlGetKey;
  }

  @Override
  public Optional<String> get(String key) {
    try {
      Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection
          .prepareStatement(sqlGetKey);
      statement.setString(1, key);
      ResultSet resultSet = statement.executeQuery();
      String value = null;
      if (resultSet.next()) {
        value = resultSet.getString(1);
      }
      resultSet.close();
      connection.close();
      return Optional.ofNullable(value);
    } catch (SQLException e) {
      throw new RuntimeException("cannot fetch key from DataSource", e);
    }
  }

  @Override
  public Set<String> keySet() {
    try {
      Set<String> keys = new TreeSet<>();
      Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection
          .prepareStatement(sqlKeySet);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        keys.add(resultSet.getString(1));
      }
      resultSet.close();
      connection.close();
      return keys;
    } catch (SQLException e) {
      throw new RuntimeException("cannot fetch all key from DataSource", e);
    }
  }
}
