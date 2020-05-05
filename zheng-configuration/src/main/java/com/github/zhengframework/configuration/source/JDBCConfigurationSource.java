package com.github.zhengframework.configuration.source;

/*-
 * #%L
 * zheng-configuration
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.zhengframework.configuration.environment.Environment;
import com.github.zhengframework.configuration.ex.ConfigurationSourceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;

public class JDBCConfigurationSource extends AbstractConfigurationSource {

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
  protected Map<String, String> getConfigurationInternal(Environment environment) {
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
