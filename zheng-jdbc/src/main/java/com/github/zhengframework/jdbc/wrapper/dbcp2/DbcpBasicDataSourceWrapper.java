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
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import javax.inject.Named;
import org.apache.commons.dbcp2.BasicDataSource;

public class DbcpBasicDataSourceWrapper extends DataSourceWrapper {

  private BasicDataSource origDataSource = new BasicDataSource();

  public BasicDataSource getOrigDataSource() {
    return origDataSource;
  }

  @com.google.inject.Inject(optional = true)
  public void setAbandonedLogWriter(@Named("dbcp.abandonedLogWriter") PrintWriter logWriter) {
    origDataSource.setAbandonedLogWriter(logWriter);
  }

  @com.google.inject.Inject(optional = true)
  public void setAbandonedUsageTracking(
      @Named("dbcp.abandonedUsageTracking") boolean usageTracking) {
    origDataSource.setAbandonedUsageTracking(usageTracking);
  }

  @com.google.inject.Inject(optional = true)
  public void setAccessToUnderlyingConnectionAllowed(
      @Named("dbcp.accessToUnderlyingConnectionAllowed") boolean allow) {
    origDataSource.setAccessToUnderlyingConnectionAllowed(allow);
  }

  @com.google.inject.Inject(optional = true)
  public void setAutoCommitOnReturn(@Named("dbcp.autoCommitOnReturn") boolean autoCommitOnReturn) {
    origDataSource.setAutoCommitOnReturn(autoCommitOnReturn);
  }

  @com.google.inject.Inject(optional = true)
  public void setCacheState(@Named("dbcp.cacheState") boolean cacheState) {
    origDataSource.setCacheState(cacheState);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionFactoryClassName(
      @Named("dbcp.connectionFactoryClassName") String connectionFactoryClassName) {
    origDataSource.setConnectionFactoryClassName(connectionFactoryClassName);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionInitSqls(
      @Named("dbcp.connectionInitSqls") Collection<String> connectionInitSqls) {
    origDataSource.setConnectionInitSqls(connectionInitSqls);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionProperties(
      @Named("dbcp.connectionProperties") String connectionProperties) {
    origDataSource.setConnectionProperties(connectionProperties);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultAutoCommit(@Named("dbcp.defaultAutoCommit") Boolean v) {
    origDataSource.setDefaultAutoCommit(v);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultCatalog(@Named("dbcp.defaultCatalog") String defaultCatalog) {
    origDataSource.setDefaultCatalog(defaultCatalog);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultQueryTimeout(
      @Named("dbcp.defaultQueryTimeout") Integer defaultQueryTimeoutSeconds) {
    origDataSource.setDefaultQueryTimeout(defaultQueryTimeoutSeconds);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultReadOnly(@Named("dbcp.defaultReadOnly") Boolean v) {
    origDataSource.setDefaultReadOnly(v);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultSchema(@Named("dbcp.defaultSchema") String defaultSchema) {
    origDataSource.setDefaultSchema(defaultSchema);
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
  public void setMaxIdle(@Named("dbcp.maxIdle") int maxIdle) {
    origDataSource.setMaxIdle(maxIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxOpenPreparedStatements(
      @Named("dbcp.maxOpenPreparedStatements") int maxOpenStatements) {
    origDataSource.setMaxOpenPreparedStatements(maxOpenStatements);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxTotal(@Named("dbcp.maxTotal") int maxTotal) {
    origDataSource.setMaxTotal(maxTotal);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxWaitMillis(@Named("dbcp.maxWaitMillis") long maxWaitMillis) {
    origDataSource.setMaxWaitMillis(maxWaitMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setMinEvictableIdleTimeMillis(
      @Named("dbcp.minEvictableIdleTimeMillis") long minEvictableIdleTimeMillis) {
    origDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setMinIdle(@Named("dbcp.minIdle") int minIdle) {
    origDataSource.setMinIdle(minIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setNumTestsPerEvictionRun(
      @Named("dbcp.numTestsPerEvictionRun") int numTestsPerEvictionRun) {
    origDataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
  }

  @com.google.inject.Inject(optional = true)
  public void setPoolPreparedStatements(
      @Named("dbcp.poolPreparedStatements") boolean poolingStatements) {
    origDataSource.setPoolPreparedStatements(poolingStatements);
  }

  @com.google.inject.Inject(optional = true)
  public void setRemoveAbandonedOnBorrow(
      @Named("dbcp.removeAbandonedOnBorrow") boolean removeAbandonedOnBorrow) {
    origDataSource.setRemoveAbandonedOnBorrow(removeAbandonedOnBorrow);
  }

  @com.google.inject.Inject(optional = true)
  public void setRemoveAbandonedOnMaintenance(
      @Named("dbcp.removeAbandonedOnMaintenance") boolean removeAbandonedOnMaintenance) {
    origDataSource.setRemoveAbandonedOnMaintenance(removeAbandonedOnMaintenance);
  }

  @com.google.inject.Inject(optional = true)
  public void setRemoveAbandonedTimeout(
      @Named("dbcp.removeAbandonedTimeout") int removeAbandonedTimeout) {
    origDataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
  }

  @com.google.inject.Inject(optional = true)
  public void setRollbackOnReturn(@Named("dbcp.rollbackOnReturn") boolean rollbackOnReturn) {
    origDataSource.setRollbackOnReturn(rollbackOnReturn);
  }

  @com.google.inject.Inject(optional = true)
  public void setSoftMinEvictableIdleTimeMillis(
      @Named("dbcp.softMinEvictableIdleTimeMillis") long softMinEvictableIdleTimeMillis) {
    origDataSource.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setTestOnBorrow(@Named("dbcp.testOnBorrow") boolean testOnBorrow) {
    origDataSource.setTestOnBorrow(testOnBorrow);
  }

  @com.google.inject.Inject(optional = true)
  public void setTestOnCreate(@Named("dbcp.testOnCreate") boolean testOnCreate) {
    origDataSource.setTestOnCreate(testOnCreate);
  }

  @com.google.inject.Inject(optional = true)
  public void setTestOnReturn(@Named("dbcp.testOnReturn") boolean testOnReturn) {
    origDataSource.setTestOnReturn(testOnReturn);
  }

  @com.google.inject.Inject(optional = true)
  public void setTestWhileIdle(@Named("dbcp.testWhileIdle") boolean testWhileIdle) {
    origDataSource.setTestWhileIdle(testWhileIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setTimeBetweenEvictionRunsMillis(
      @Named("dbcp.timeBetweenEvictionRunsMillis") long timeBetweenEvictionRunsMillis) {
    origDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
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
  public void setDisconnectionSqlCodes(
      @Named("dbcp.disconnectionSqlCodes") Collection<String> disconnectionSqlCodes) {
    origDataSource.setDisconnectionSqlCodes(disconnectionSqlCodes);
  }

  @com.google.inject.Inject(optional = true)
  public void setEvictionPolicyClassName(
      @Named("dbcp.evictionPolicyClassName") String evictionPolicyClassName) {
    origDataSource.setEvictionPolicyClassName(evictionPolicyClassName);
  }

  @com.google.inject.Inject(optional = true)
  public void setFastFailValidation(@Named("dbcp.fastFailValidation") boolean fastFailValidation) {
    origDataSource.setFastFailValidation(fastFailValidation);
  }

  @com.google.inject.Inject(optional = true)
  public void setInitialSize(@Named("dbcp.initialSize") int initialSize) {
    origDataSource.setInitialSize(initialSize);
  }

  @com.google.inject.Inject(optional = true)
  public void setJmxName(@Named("dbcp.jmxName") String jmxName) {
    origDataSource.setJmxName(jmxName);
  }

  @com.google.inject.Inject(optional = true)
  public void setLifo(@Named("dbcp.lifo") boolean lifo) {
    origDataSource.setLifo(lifo);
  }

  @com.google.inject.Inject(optional = true)
  public void setLogAbandoned(@Named("dbcp.logAbandoned") boolean logAbandoned) {
    origDataSource.setLogAbandoned(logAbandoned);
  }

  @com.google.inject.Inject(optional = true)
  public void setLogExpiredConnections(
      @Named("dbcp.logExpiredConnections") boolean logExpiredConnections) {
    origDataSource.setLogExpiredConnections(logExpiredConnections);
  }

  @com.google.inject.Inject(optional = true)
  public void setValidationQuery(@Named("dbcp.validationQuery") String validationQuery) {
    origDataSource.setValidationQuery(validationQuery);
  }

  @Override
  public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
    super.setDataSourceConfig(dataSourceConfig);
    origDataSource.setDriverClassLoader(Thread.currentThread().getContextClassLoader());
    origDataSource
        .setDriverClassName(Objects.requireNonNull(dataSourceConfig.getDriverClassName()));
    origDataSource.setUrl(dataSourceConfig.getUrl());
    origDataSource.setUsername(dataSourceConfig.getUsername());
    origDataSource.setPassword(dataSourceConfig.getPassword());
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
