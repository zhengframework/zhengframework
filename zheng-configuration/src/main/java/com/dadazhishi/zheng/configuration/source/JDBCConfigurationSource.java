package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.environment.Environment;
import com.dadazhishi.zheng.configuration.ex.ConfigurationSourceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;

public class JDBCConfigurationSource implements ConfigurationSource {

  private final DataSource dataSource;
  private final String fetchSQL;

  public JDBCConfigurationSource(DataSource dataSource, String fetchSQL) {
    this.dataSource = dataSource;
    this.fetchSQL = fetchSQL;
  }

  @Override
  public void init() {

  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(fetchSQL)) {
        String environmentName = StringUtils.trim(environment.getName());
        if (StringUtils.isEmpty(environmentName)) {
          preparedStatement.setString(1, environmentName);
        }
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          Map<String, String> map = new HashMap<>();
          while (resultSet.next()) {
            String key = resultSet.getString(1);
            String value = resultSet.getString(2);
            map.put(key, value);
          }
          return map;
        }
      }
    } catch (SQLException e) {
      throw new ConfigurationSourceException("fail load configuration from dataSource", e);
    }
  }
}
