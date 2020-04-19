package com.github.zhengframework.healthcheck.datasource;

import com.github.zhengframework.healthcheck.NamedHealthCheck;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DataSourceHealthCheck extends NamedHealthCheck {

  private final String dataSourceName;
  private final DataSource dataSource;
  private final String testQuery;

  public DataSourceHealthCheck(String dataSourceName, DataSource dataSource,
      String testQuery) {
    this.dataSourceName = dataSourceName;
    this.dataSource = dataSource;
    this.testQuery = testQuery;
  }

  public DataSourceHealthCheck(String dataSourceName, DataSource dataSource) {
    this(dataSourceName, dataSource, "select 1");
  }

  @Override
  public String getName() {
    return "DataSourceHealthCheck-" + dataSourceName;
  }

  @Override
  protected Result check() throws Exception {
    try {
      if (dataSourceCheck(dataSource)) {
        return Result.healthy();
      }
    } catch (SQLException e) {
      return Result.unhealthy(e);
    }
    throw new IllegalStateException();
  }

  protected boolean dataSourceCheck(DataSource dataSource) throws SQLException {
    Connection connection = dataSource.getConnection();
    PreparedStatement preparedStatement = connection.prepareStatement(testQuery);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.close();
    preparedStatement.close();
    connection.close();
    return true;
  }

}
