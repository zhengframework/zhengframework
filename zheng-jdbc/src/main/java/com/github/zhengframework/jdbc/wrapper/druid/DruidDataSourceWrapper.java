package com.github.zhengframework.jdbc.wrapper.druid;

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

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.alibaba.druid.pool.ExceptionSorter;
import com.alibaba.druid.pool.ValidConnectionChecker;
import com.github.zhengframework.jdbc.DataSourceConfig;
import com.github.zhengframework.jdbc.wrapper.DataSourceWrapper;
import com.github.zhengframework.jdbc.wrapper.PropertyHelper;
import com.google.inject.Inject;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Named;
import javax.management.ObjectName;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

public class DruidDataSourceWrapper extends DataSourceWrapper {

  private DruidDataSource origDataSource = new DruidDataSource();

  public DruidDataSource getOrigDataSource() {
    return origDataSource;
  }

  @Inject(optional = true)
  public void setUseLocalSessionState(
      @Named("druid.useLocalSessionState") boolean useLocalSessionState) {
    origDataSource.setUseLocalSessionState(useLocalSessionState);
  }

  @Inject(optional = true)
  public void setStatLoggerClassName(@Named("druid.statLoggerClassName") String className) {
    origDataSource.setStatLoggerClassName(className);
  }

  @Inject(optional = true)
  public void setStatLogger(@Named("druid.statLogger") DruidDataSourceStatLogger statLogger) {
    origDataSource.setStatLogger(statLogger);
  }

  @Inject(optional = true)
  public void setTimeBetweenLogStatsMillis(
      @Named("druid.timeBetweenLogStatsMillis") long timeBetweenLogStatsMillis) {
    origDataSource.setTimeBetweenLogStatsMillis(timeBetweenLogStatsMillis);
  }

  @Inject(optional = true)
  public void setOracle(@Named("druid.oracle") boolean isOracle) {
    origDataSource.setOracle(isOracle);
  }

  @Inject(optional = true)
  public void setUseUnfairLock(@Named("druid.useUnfairLock") boolean useUnfairLock) {
    origDataSource.setUseUnfairLock(useUnfairLock);
  }

  @Inject(optional = true)
  public void setUseOracleImplicitCache(
      @Named("druid.useOracleImplicitCache") boolean useOracleImplicitCache) {
    origDataSource.setUseOracleImplicitCache(useOracleImplicitCache);
  }

  @Inject(optional = true)
  public void setTransactionQueryTimeout(
      @Named("druid.transactionQueryTimeout") int transactionQueryTimeout) {
    origDataSource.setTransactionQueryTimeout(transactionQueryTimeout);
  }

  @Inject(optional = true)
  public void setDupCloseLogEnable(@Named("druid.dupCloseLogEnable") boolean dupCloseLogEnable) {
    origDataSource.setDupCloseLogEnable(dupCloseLogEnable);
  }

  @Inject(optional = true)
  public void setObjectName(@Named("druid.objectName") ObjectName objectName) {
    origDataSource.setObjectName(objectName);
  }

  @Inject(optional = true)
  public void setTransactionThresholdMillis(
      @Named("druid.transactionThresholdMillis") long transactionThresholdMillis) {
    origDataSource.setTransactionThresholdMillis(transactionThresholdMillis);
  }

  @Inject(optional = true)
  public void setBreakAfterAcquireFailure(
      @Named("druid.breakAfterAcquireFailure") boolean breakAfterAcquireFailure) {
    origDataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
  }

  @Inject(optional = true)
  public void setConnectionErrorRetryAttempts(
      @Named("druid.connectionErrorRetryAttempts") int connectionErrorRetryAttempts) {
    origDataSource.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
  }

  @Inject(optional = true)
  public void setMaxPoolPreparedStatementPerConnectionSize(
      @Named("druid.maxPoolPreparedStatementPerConnectionSize")
          int maxPoolPreparedStatementPerConnectionSize) {
    origDataSource.setMaxPoolPreparedStatementPerConnectionSize(
        maxPoolPreparedStatementPerConnectionSize);
  }

  @Inject(optional = true)
  public void setSharePreparedStatements(
      @Named("druid.sharePreparedStatements") boolean sharePreparedStatements) {
    origDataSource.setSharePreparedStatements(sharePreparedStatements);
  }

  @Inject(optional = true)
  public void setValidConnectionChecker(
      @Named("druid.validConnectionChecker") ValidConnectionChecker validConnectionChecker) {
    origDataSource.setValidConnectionChecker(validConnectionChecker);
  }

  @Inject(optional = true)
  public void setValidConnectionCheckerClassName(
      @Named("druid.validConnectionCheckerClass") String validConnectionCheckerClass)
      throws Exception {
    origDataSource.setValidConnectionCheckerClassName(validConnectionCheckerClass);
  }

  @Inject(optional = true)
  public void setDbType(@Named("druid.dbType") String dbType) {
    origDataSource.setDbType(dbType);
  }

  @Inject(optional = true)
  public void setConnectionInitSqls(
      @Named("druid.connectionInitSqls") Collection<?> connectionInitSqls) {
    origDataSource.setConnectionInitSqls(connectionInitSqls);
  }

  @Inject(optional = true)
  public void setTimeBetweenConnectErrorMillis(
      @Named("druid.timeBetweenConnectErrorMillis") long timeBetweenConnectErrorMillis) {
    origDataSource.setTimeBetweenConnectErrorMillis(timeBetweenConnectErrorMillis);
  }

  @Inject(optional = true)
  public void setRemoveAbandonedTimeoutMillis(
      @Named("druid.removeAbandonedTimeoutMillis") long removeAbandonedTimeoutMillis) {
    origDataSource.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
  }

  @Inject(optional = true)
  public void setKeepAliveBetweenTimeMillis(
      @Named("druid.keepAliveBetweenTimeMillis") long keepAliveBetweenTimeMillis) {
    origDataSource.setKeepAliveBetweenTimeMillis(keepAliveBetweenTimeMillis);
  }

  @Inject(optional = true)
  public void setMaxEvictableIdleTimeMillis(
      @Named("druid.maxEvictableIdleTimeMillis") long maxEvictableIdleTimeMillis) {
    origDataSource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
  }

  @Inject(optional = true)
  public void setPhyTimeoutMillis(@Named("druid.phyTimeoutMillis") long phyTimeoutMillis) {
    origDataSource.setPhyTimeoutMillis(phyTimeoutMillis);
  }

  @Inject(optional = true)
  public void setPhyMaxUseCount(@Named("druid.phyMaxUseCount") long phyMaxUseCount) {
    origDataSource.setPhyMaxUseCount(phyMaxUseCount);
  }

  @Inject(optional = true)
  public void setMaxWaitThreadCount(@Named("druid.maxWaitThreadCount") int maxWaitThreadCount) {
    origDataSource.setMaxWaitThreadCount(maxWaitThreadCount);
  }

  @Inject(optional = true)
  public void setDefaultReadOnly(@Named("druid.defaultReadOnly") Boolean defaultReadOnly) {
    origDataSource.setDefaultReadOnly(defaultReadOnly);
  }

  @Inject(optional = true)
  public void setDefaultTransactionIsolation(
      @Named("druid.defaultTransactionIsolation") Integer defaultTransactionIsolation) {
    origDataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
  }

  @Inject(optional = true)
  public void setPasswordCallback(
      @Named("druid.passwordCallback") PasswordCallback passwordCallback) {
    origDataSource.setPasswordCallback(passwordCallback);
  }

  @Inject(optional = true)
  public void setPasswordCallbackClassName(
      @Named("druid.passwordCallbackClassName") String passwordCallbackClassName) throws Exception {
    origDataSource.setPasswordCallbackClassName(passwordCallbackClassName);
  }

  @Inject(optional = true)
  public void setUserCallback(@Named("druid.userCallback") NameCallback userCallback) {
    origDataSource.setUserCallback(userCallback);
  }

  @Inject(optional = true)
  public void setInitVariants(@Named("druid.initVariants") boolean initVariants) {
    origDataSource.setInitVariants(initVariants);
  }

  @Inject(optional = true)
  public void setInitGlobalVariants(@Named("druid.initGlobalVariants") boolean initGlobalVariants) {
    origDataSource.setInitGlobalVariants(initGlobalVariants);
  }

  @Inject(optional = true)
  public void setQueryTimeout(@Named("druid.queryTimeout") int seconds) {
    origDataSource.setQueryTimeout(seconds);
  }

  @Inject(optional = true)
  public void setName(@Named("druid.name") String name) {
    origDataSource.setName(name);
  }

  @Inject(optional = true)
  public void setNotFullTimeoutRetryCount(
      @Named("druid.notFullTimeoutRetryCount") int notFullTimeoutRetryCount) {
    origDataSource.setNotFullTimeoutRetryCount(notFullTimeoutRetryCount);
  }

  @Inject(optional = true)
  public void setExceptionSorter(@Named("druid.exceptionSorter") ExceptionSorter exceptionSorter) {
    origDataSource.setExceptionSorter(exceptionSorter);
  }

  @Inject(optional = true)
  public void setProxyFilters(@Named("druid.proxyFilters") List<Filter> filters) {
    origDataSource.setProxyFilters(filters);
  }

  @Inject(optional = true)
  public void setClearFiltersEnable(@Named("druid.clearFiltersEnable") boolean clearFiltersEnable) {
    origDataSource.setClearFiltersEnable(clearFiltersEnable);
  }

  @Inject(optional = true)
  public void setAsyncCloseConnectionEnable(
      @Named("druid.asyncCloseConnectionEnable") boolean asyncCloseConnectionEnable) {
    origDataSource.setAsyncCloseConnectionEnable(asyncCloseConnectionEnable);
  }

  @Inject(optional = true)
  public void setCreateScheduler(
      @Named("druid.createScheduler") ScheduledExecutorService createScheduler) {
    origDataSource.setCreateScheduler(createScheduler);
  }

  @Inject(optional = true)
  public void setDestroyScheduler(
      @Named("druid.destroyScheduler") ScheduledExecutorService destroyScheduler) {
    origDataSource.setDestroyScheduler(destroyScheduler);
  }

  @Inject(optional = true)
  public void setMaxCreateTaskCount(@Named("druid.maxCreateTaskCount") int maxCreateTaskCount) {
    origDataSource.setMaxCreateTaskCount(maxCreateTaskCount);
  }

  @Inject(optional = true)
  public void setFailFast(@Named("druid.failFast") boolean failFast) {
    origDataSource.setFailFast(failFast);
  }

  @Inject(optional = true)
  public void setOnFatalErrorMaxActive(
      @Named("druid.onFatalErrorMaxActive") int onFatalErrorMaxActive) {
    origDataSource.setOnFatalErrorMaxActive(onFatalErrorMaxActive);
  }

  @Inject(optional = true)
  public void setInitExceptionThrow(@Named("druid.initExceptionThrow") boolean initExceptionThrow) {
    origDataSource.setInitExceptionThrow(initExceptionThrow);
  }

  @Inject(optional = true)
  public void setDefaultAutoCommit(
      @Named("druid.defaultAutoCommit") final boolean defaultAutoCommit) {
    origDataSource.setDefaultAutoCommit(defaultAutoCommit);
  }

  @Inject(optional = true)
  public void setDefaultReadOnly(@Named("druid.defaultReadOnly") final boolean defaultReadOnly) {
    origDataSource.setDefaultReadOnly(defaultReadOnly);
  }

  @Inject(optional = true)
  public void setDefaultTransactionIsolation(
      @Named("druid.defaultTransactionIsolation") final int defaultTransactionIsolation) {
    origDataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
  }

  @Inject(optional = true)
  public void setDefaultCatalog(@Named("druid.defaultCatalog") final String defaultCatalog) {
    origDataSource.setDefaultCatalog(defaultCatalog);
  }

  @Inject(optional = true)
  public void setMaxActive(@Named("druid.maxActive") final int maxActive) {
    origDataSource.setMaxActive(maxActive);
  }

  @Inject(optional = true)
  public void setMinIdle(@Named("druid.minIdle") final int minIdle) {
    origDataSource.setMinIdle(minIdle);
  }

  @Inject(optional = true)
  public void setInitialSize(@Named("druid.initialSize") final int initialSize) {
    origDataSource.setInitialSize(initialSize);
  }

  @Inject(optional = true)
  public void setMaxWait(@Named("druid.maxWait") final long maxWait) {
    origDataSource.setMaxWait(maxWait);
  }

  @Inject(optional = true)
  public void setTestOnBorrow(@Named("druid.testOnBorrow") final boolean testOnBorrow) {
    origDataSource.setTestOnBorrow(testOnBorrow);
  }

  @Inject(optional = true)
  public void setTestOnReturn(@Named("druid.testOnReturn") final boolean testOnReturn) {
    origDataSource.setTestOnReturn(testOnReturn);
  }

  @Inject(optional = true)
  public void setTimeBetweenEvictionRunsMillis(
      @Named("druid.timeBetweenEvictionRunsMillis") final long timeBetweenEvictionRunsMillis) {
    origDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
  }

  @Inject(optional = true)
  public void setMinEvictableIdleTimeMillis(
      @Named("druid.minEvictableIdleTimeMillis") final long minEvictableIdleTimeMillis) {
    origDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
  }

  @Inject(optional = true)
  public void setTestWhileIdle(@Named("druid.testWhileIdle") final boolean testWhileIdle) {
    origDataSource.setTestWhileIdle(testWhileIdle);
  }

  @Inject(optional = true)
  public void setValidationQuery(@Named("druid.validationQuery") final String validationQuery) {
    origDataSource.setValidationQuery(validationQuery);
  }

  @Inject(optional = true)
  public void setValidationQueryTimeout(
      @Named("druid.validationQueryTimeout") final int validationQueryTimeout) {
    origDataSource.setValidationQueryTimeout(validationQueryTimeout);
  }

  @Inject(optional = true)
  public void setAccessToUnderlyingConnectionAllowed(
      @Named("druid.accessToUnderlyingConnectionAllowed") final boolean accessToUnderlyingConnectionAllowed) {
    origDataSource.setAccessToUnderlyingConnectionAllowed(accessToUnderlyingConnectionAllowed);
  }

  @Inject(optional = true)
  public void setRemoveAbandoned(@Named("druid.removeAbandoned") final boolean removeAbandoned) {
    origDataSource.setRemoveAbandoned(removeAbandoned);
  }

  @Inject(optional = true)
  public void setRemoveAbandonedTimeout(
      @Named("druid.removeAbandonedTimeout") final int removeAbandonedTimeout) {
    origDataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
  }

  @Inject(optional = true)
  public void setLogAbandoned(@Named("druid.logAbandoned") final boolean logAbandoned) {
    origDataSource.setLogAbandoned(logAbandoned);
  }

  @Inject(optional = true)
  public void setPoolPreparedStatements(
      @Named("druid.poolPreparedStatements") final boolean poolPreparedStatements) {
    origDataSource.setPoolPreparedStatements(poolPreparedStatements);
  }

  @Inject(optional = true)
  public void setMaxOpenPreparedStatements(
      @Named("druid.maxOpenPreparedStatements") final int maxOpenPreparedStatements) {
    origDataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
  }

  @Inject(optional = true)
  public void setConnectProperties(
      @Named("druid.connectProperties") final Properties connectionProperties) {
    origDataSource.setConnectProperties(connectionProperties);
  }

  @Inject(optional = true)
  public void setConnectionProperties(
      @Named("druid.connectionProperties") final String connectionProperties) {
    origDataSource.setConnectionProperties(connectionProperties);
  }

  @Inject(optional = true)
  public void setFilters(@Named("druid.filters") final String filters) throws SQLException {
    origDataSource.setFilters(filters);
  }

  @Inject(optional = true)
  public void setExceptionSorter(@Named("druid.exceptionSorter") final String exceptionSorter)
      throws SQLException {
    origDataSource.setExceptionSorter(exceptionSorter);
  }

  @Inject(optional = true)
  public void setExceptionSorterClassName(
      @Named("druid.exceptionSorterClassName") final String exceptionSorterClassName)
      throws Exception {
    origDataSource.setExceptionSorterClassName(exceptionSorterClassName);
  }

  @Override
  public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
    super.setDataSourceConfig(dataSourceConfig);
    origDataSource.setDriverClassLoader(Thread.currentThread().getContextClassLoader());
    origDataSource.setDriverClassName(
        Objects.requireNonNull(dataSourceConfig.getDriverClassName()));
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
