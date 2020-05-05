package com.github.zhengframework.jdbc.wrapper.hikari;

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
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import javax.inject.Named;

public class HikariDataSourceWrapper extends DataSourceWrapper {

  private HikariDataSource origDataSource;
  private HikariConfig configuration = new HikariConfig();

  public HikariDataSourceWrapper() {
  }

  public HikariDataSource getOrigDataSource() {
    return origDataSource;
  }

  @com.google.inject.Inject(optional = true)
  public void setAllowPoolSuspension(
      @Named("hikaricp.allowPoolSuspension") boolean allowPoolSuspension) {
    configuration.setAllowPoolSuspension(allowPoolSuspension);
  }

  @com.google.inject.Inject(optional = true)
  public void setAutoCommit(@Named("hikaricp.autoCommit") boolean autoCommit) {
    configuration.setAutoCommit(autoCommit);
  }

  @com.google.inject.Inject(optional = true)
  public void setCatalog(@Named("hikaricp.catalog") String catalog) {
    configuration.setCatalog(catalog);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionInitSql(@Named("hikaricp.connectionInitSql") String connectionInitSql) {
    configuration.setConnectionInitSql(connectionInitSql);
  }

  @com.google.inject.Inject(optional = true)
  public void setDataSourceJNDI(@Named("hikaricp.dataSourceJNDI") String dataSourceJNDI) {
    configuration.setDataSourceJNDI(dataSourceJNDI);
  }

  @com.google.inject.Inject(optional = true)
  public void setDataSourceProperties(
      @Named("hikaricp.dataSourceProperties") Properties dsProperties) {
    configuration.setDataSourceProperties(dsProperties);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionTestQuery(
      @Named("hikaricp.connectionTestQuery") String connectionTestQuery) {
    configuration.setConnectionTestQuery(connectionTestQuery);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionTimeout(@Named("hikaricp.connectionTimeout") long connectionTimeout) {
    configuration.setConnectionTimeout(connectionTimeout);
  }

  @com.google.inject.Inject(optional = true)
  public void setHealthCheckProperties(
      @Named("hikaricp.healthCheckProperties") Properties healthCheckProperties) {
    configuration.setHealthCheckProperties(healthCheckProperties);
  }

  @com.google.inject.Inject(optional = true)
  public void setHealthCheckRegistry(
      @Named("hikaricp.healthCheckRegistry") Object healthCheckRegistry) {
    configuration.setHealthCheckRegistry(healthCheckRegistry);
  }

  @com.google.inject.Inject(optional = true)
  public void setIdleTimeout(@Named("hikaricp.idleTimeout") long idleTimeout) {
    configuration.setIdleTimeout(idleTimeout);
  }

  @com.google.inject.Inject(optional = true)
  public void setInitializationFailTimeout(
      @Named("hikaricp.initializationFailTimeout") long initializationFailTimeout) {
    configuration.setInitializationFailTimeout(initializationFailTimeout);
  }

  @com.google.inject.Inject(optional = true)
  public void setIsolateInternalQueries(
      @Named("hikaricp.isolateInternalQueries") boolean isolateInternalQueries) {
    configuration.setIsolateInternalQueries(isolateInternalQueries);
  }

  @com.google.inject.Inject(optional = true)
  public void setLeakDetectionThreshold(
      @Named("hikaricp.leakDetectionThreshold") long leakDetectionThreshold) {
    configuration.setLeakDetectionThreshold(leakDetectionThreshold);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxLifetime(@Named("hikaricp.maxLifetime") long maxLifetime) {
    configuration.setMaxLifetime(maxLifetime);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaximumPoolSize(@Named("hikaricp.maximumPoolSize") int maxPoolSize) {
    configuration.setMaximumPoolSize(maxPoolSize);
  }

  @com.google.inject.Inject(optional = true)
  public void setMetricRegistry(@Named("hikaricp.metricRegistry") Object metricRegistry) {
    configuration.setMetricRegistry(metricRegistry);
  }

  @com.google.inject.Inject(optional = true)
  public void setMetricsTrackerFactory(
      @Named("hikaricp.metricsTrackerFactory") MetricsTrackerFactory metricsTrackerFactory) {
    configuration.setMetricsTrackerFactory(metricsTrackerFactory);
  }

  @com.google.inject.Inject(optional = true)
  public void setMinimumIdle(@Named("hikaricp.minimumIdle") int minIdle) {
    configuration.setMinimumIdle(minIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setPoolName(@Named("hikaricp.poolName") String poolName) {
    configuration.setPoolName(poolName);
  }

  @com.google.inject.Inject(optional = true)
  public void setReadOnly(@Named("hikaricp.readOnly") boolean readOnly) {
    configuration.setReadOnly(readOnly);
  }

  @com.google.inject.Inject(optional = true)
  public void setRegisterMbeans(@Named("hikaricp.registerMbeans") boolean registerMbeans) {
    configuration.setRegisterMbeans(registerMbeans);
  }

  @com.google.inject.Inject(optional = true)
  public void setScheduledExecutor(
      @Named("hikaricp.scheduledExecutor") ScheduledExecutorService scheduledExecutorService) {
    configuration.setScheduledExecutor(scheduledExecutorService);
  }

  @com.google.inject.Inject(optional = true)
  public void setSchema(@Named("hikaricp.schema") String schema) {
    configuration.setSchema(schema);
  }

  @com.google.inject.Inject(optional = true)
  public void setThreadFactory(@Named("hikaricp.threadFactory") ThreadFactory threadFactory) {
    configuration.setThreadFactory(threadFactory);
  }

  @com.google.inject.Inject(optional = true)
  public void setTransactionIsolation(
      @Named("hikaricp.transactionIsolation") String transactionIsolation) {
    configuration.setTransactionIsolation(transactionIsolation);
  }

  @com.google.inject.Inject(optional = true)
  public void setValidationTimeout(@Named("hikaricp.validationTimeout") long validationTimeout) {
    configuration.setValidationTimeout(validationTimeout);
  }

  @Override
  public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
    super.setDataSourceConfig(dataSourceConfig);
    configuration.setDriverClassName(Objects.requireNonNull(dataSourceConfig.getDriverClassName()));
    configuration.setJdbcUrl(Objects.requireNonNull(dataSourceConfig.getUrl()));
    configuration.setUsername(dataSourceConfig.getUsername());
    configuration.setPassword(dataSourceConfig.getPassword());

    Map<String, String> properties = dataSourceConfig.getProperties();
    PropertyHelper.setTargetFromProperties(this, properties);
  }

  @Override
  public void init() throws Exception {
    DataSourceConfig dataSourceConfig = getDataSourceConfig();
    origDataSource = new HikariDataSource(configuration);
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
