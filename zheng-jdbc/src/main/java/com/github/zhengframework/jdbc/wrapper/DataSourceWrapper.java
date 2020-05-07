package com.github.zhengframework.jdbc.wrapper;

/*-
 * #%L
 * zheng-jdbc
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

import com.github.zhengframework.jdbc.DataSourceConfig;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

public abstract class DataSourceWrapper implements DataSource {

  private DataSource dataSource;
  private DataSourceConfig dataSourceConfig;

  public DataSource getDataSource() {
    return dataSource;
  }

  protected void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public DataSourceConfig getDataSourceConfig() {
    return dataSourceConfig;
  }

  public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
    this.dataSourceConfig = dataSourceConfig;
  }

  public abstract void init() throws Exception;

  @Override
  public Connection getConnection() throws SQLException {
    return getDataSource().getConnection();
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return getDataSource().getConnection(username, password);
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return getDataSource().unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return getDataSource().isWrapperFor(iface);
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return getDataSource().getLogWriter();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    getDataSource().setLogWriter(out);
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return getDataSource().getLoginTimeout();
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    getDataSource().setLoginTimeout(seconds);
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return getDataSource().getParentLogger();
  }

  public abstract void close() throws Exception;
}
