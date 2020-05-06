package com.github.zhengframework.jdbc.wrapper.dbcp2;

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
import com.github.zhengframework.jdbc.wrapper.DataSourceWrapper;
import com.github.zhengframework.jdbc.wrapper.PropertyHelper;
import java.util.Map;
import java.util.Objects;
import javax.inject.Named;
import javax.sql.ConnectionPoolDataSource;
import org.apache.commons.dbcp2.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;

public class DbcpSharedDataSourceWrapper extends DataSourceWrapper {

  private SharedPoolDataSource origDataSource = new SharedPoolDataSource();

  public SharedPoolDataSource getOrigDataSource() {
    return origDataSource;
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultBlockWhenExhausted(
      @Named("dbcp.defaultBlockWhenExhausted") boolean blockWhenExhausted) {
    origDataSource.setDefaultBlockWhenExhausted(blockWhenExhausted);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultEvictionPolicyClassName(
      @Named("dbcp.defaultEvictionPolicyClassName") String evictionPolicyClassName) {
    origDataSource.setDefaultEvictionPolicyClassName(evictionPolicyClassName);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultLifo(@Named("dbcp.defaultLifo") boolean lifo) {
    origDataSource.setDefaultLifo(lifo);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxWaitMillis(@Named("dbcp.defaultMaxWaitMillis") long maxWaitMillis) {
    origDataSource.setDefaultMaxWaitMillis(maxWaitMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultMinEvictableIdleTimeMillis(
      @Named("dbcp.defaultMinEvictableIdleTimeMillis") long minEvictableIdleTimeMillis) {
    origDataSource.setDefaultMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultMinIdle(@Named("dbcp.defaultMinIdle") int minIdle) {
    origDataSource.setDefaultMinIdle(minIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultSoftMinEvictableIdleTimeMillis(
      @Named("dbcp.defaultSoftMinEvictableIdleTimeMillis") long softMinEvictableIdleTimeMillis) {
    origDataSource.setDefaultSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTestOnCreate(@Named("dbcp.defaultTestOnCreate") boolean testOnCreate) {
    origDataSource.setDefaultTestOnCreate(testOnCreate);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTimeBetweenEvictionRunsMillis(
      @Named("dbcp.defaultTimeBetweenEvictionRunsMillis") long timeBetweenEvictionRunsMillis) {
    origDataSource.setDefaultTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultAutoCommit(@Named("dbcp.defaultAutoCommit") Boolean v) {
    origDataSource.setDefaultAutoCommit(v);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultReadOnly(@Named("dbcp.defaultReadOnly") Boolean v) {
    origDataSource.setDefaultReadOnly(v);
  }

  @com.google.inject.Inject(optional = true)
  public void setValidationQueryTimeout(
      @Named("dbcp.validationQueryTimeout") int validationQueryTimeoutSeconds) {
    origDataSource.setValidationQueryTimeout(validationQueryTimeoutSeconds);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxConnLifetimeMillis(
      @Named("dbcp.maxConnLifetimeMillis") long maxConnLifetimeMillis) {
    origDataSource.setMaxConnLifetimeMillis(maxConnLifetimeMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionPoolDataSource(
      @Named("dbcp.connectionPoolDataSource") ConnectionPoolDataSource cpds) {
    origDataSource.setConnectionPoolDataSource(cpds);
  }

  @com.google.inject.Inject(optional = true)
  public void setDataSourceName(@Named("dbcp.name") String name) {
    origDataSource.setDataSourceName(name);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultAutoCommit(@Named("JDBC.defaultAutoCommit") boolean autoCommit) {
    origDataSource.setDefaultAutoCommit(autoCommit);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultReadOnly(@Named("dbcp.defaultReadOnly") boolean defaultReadOnly) {
    origDataSource.setDefaultReadOnly(defaultReadOnly);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTransactionIsolation(
      @Named("dbcp.defaultTransactionIsolation") int defaultTransactionIsolation) {
    origDataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
  }

  @com.google.inject.Inject(optional = true)
  public void setDescription(@Named("dbcp.description") String description) {
    origDataSource.setDescription(description);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultMinEvictableIdleTimeMillis(
      @Named("dbcp.defaultMinEvictableIdleTimeMillis") int defaultMinEvictableIdleTimeMillis) {
    origDataSource.setDefaultMinEvictableIdleTimeMillis(defaultMinEvictableIdleTimeMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultNumTestsPerEvictionRun(
      @Named("dbcp.defaultNumTestsPerEvictionRun") int defaultNumTestsPerEvictionRun) {
    origDataSource.setDefaultNumTestsPerEvictionRun(defaultNumTestsPerEvictionRun);
  }

  @com.google.inject.Inject(optional = true)
  public void setRollbackAfterValidation(
      @Named("dbcp.rollbackAfterValidation") boolean rollbackAfterValidation) {
    origDataSource.setRollbackAfterValidation(rollbackAfterValidation);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTestOnBorrow(
      @Named("dbcp.defaultTestOnBorrow") boolean defaultTestOnBorrow) {
    origDataSource.setDefaultTestOnBorrow(defaultTestOnBorrow);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTestOnReturn(
      @Named("dbcp.defaultTestOnReturn") boolean defaultTestOnReturn) {
    origDataSource.setDefaultTestOnReturn(defaultTestOnReturn);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTestWhileIdle(
      @Named("dbcp.defaultTestWhileIdle") boolean defaultTestWhileIdle) {
    origDataSource.setDefaultTestWhileIdle(defaultTestWhileIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTimeBetweenEvictionRunsMillis(
      @Named("dbcp.defaultTimeBetweenEvictionRunsMillis")
          int defaultTimeBetweenEvictionRunsMillis) {
    origDataSource.setDefaultTimeBetweenEvictionRunsMillis(defaultTimeBetweenEvictionRunsMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setValidationQuery(@Named("dbcp.validationQuery") String validationQuery) {
    origDataSource.setValidationQuery(validationQuery);
  }

  /**
   * Sets the default max total.
   *
   * @param defaultMaxTotal the new default max total
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxTotal(@Named("dbcp.defaultMaxTotal") final int defaultMaxTotal) {
    origDataSource.setDefaultMaxTotal(defaultMaxTotal);
  }

  /**
   * Sets the default max idle.
   *
   * @param defaultMaxIdle the new default max idle
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxIdle(@Named("dbcp.defaultMaxIdle") final int defaultMaxIdle) {
    origDataSource.setDefaultMaxIdle(defaultMaxIdle);
  }

  /**
   * Sets the default max wait in milliseconds.
   *
   * @param defaultMaxWaitMillis the new default max wait in milliseconds
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxWaitMillis(
      @Named("dbcp.defaultMaxWaitMillis") final int defaultMaxWaitMillis) {
    origDataSource.setDefaultMaxWaitMillis(defaultMaxWaitMillis);
  }

  @Override
  public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
    super.setDataSourceConfig(dataSourceConfig);
    DriverAdapterCPDS driverAdapterCPDS = new DriverAdapterCPDS();
    try {
      driverAdapterCPDS.setDriver(Objects.requireNonNull(dataSourceConfig.getDriverClassName()));
      driverAdapterCPDS.setUrl(Objects.requireNonNull(dataSourceConfig.getUrl()));
      driverAdapterCPDS.setUser(dataSourceConfig.getUsername());
      driverAdapterCPDS.setPassword(dataSourceConfig.getPassword());
      setConnectionPoolDataSource(driverAdapterCPDS);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    Map<String, String> properties = dataSourceConfig.getProperties();
    PropertyHelper.setTargetFromProperties(this, properties);
  }

  @Override
  public void init() throws Exception {
    DataSourceConfig dataSourceConfig = getDataSourceConfig();
    if (dataSourceConfig.getLoginTimeout() != null) {
      origDataSource.setLoginTimeout(dataSourceConfig.getLoginTimeout());
    }
    setDataSource(origDataSource);
  }

  @Override
  public void close() throws Exception {
    origDataSource.close();
  }
}
