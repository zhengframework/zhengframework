package com.github.zhengframework.jdbc.wrapper.hikari;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.github.zhengframework.jdbc.DataSourceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;
import java.util.Properties;
import org.junit.Test;

public class HikariDataSourceWrapperTest {

  @Test
  public void get() throws Exception {
    final String driverClassName = "org.h2.Driver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";

    final int loginTimeout = 1; // Seconds

    final boolean allowPoolSuspension = true;
    final boolean autoCommit = true;

    final String catalog = "custom";
    final String connectionInitSql = "CREATE SCHEMA IF NOT EXISTS TEST; SET SCHEMA TEST;";
    final String connectionTestQuery = "Commit;";
    final long connectionTimeoutMs = 1000L;

    final Properties healthCheckProperties = new Properties();
    healthCheckProperties.put("boom", "goes the dynamite");
    final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    final long idleTimeoutMs = 42L;
    final long initializationFailTimeout = 2L;
    final boolean isolateInternalQueries = true;

    final long leakDetectionThresholdMs = 2000L; // 2000ms

    final long maxLifetimeMs = 30000L;
    final long maxPoolSize = 10L;

    final MetricRegistry metricRegistry = new MetricRegistry();
    final MetricsTrackerFactory metricsTrackerFactory =
        new CodahaleMetricsTrackerFactory(metricRegistry);

    final long minimumIdle = 10L;

    final String poolName = "Pool";

    final boolean readOnly = false;

    final String schema = "PUBLIC";

    final long validationTimeoutMs = 250; // Min is 250ms

    Injector injector =
        Guice.createInjector(
            new AbstractModule() {
              @Override
              protected void configure() {

                bindConstant()
                    .annotatedWith(Names.named("hikaricp.allowPoolSuspension"))
                    .to(allowPoolSuspension);
                bindConstant().annotatedWith(Names.named("hikaricp.autoCommit")).to(autoCommit);

                bindConstant().annotatedWith(Names.named("hikaricp.catalog")).to(catalog);
                bindConstant()
                    .annotatedWith(Names.named("hikaricp.connectionInitSql"))
                    .to(connectionInitSql);
                bindConstant()
                    .annotatedWith(Names.named("hikaricp.connectionTestQuery"))
                    .to(connectionTestQuery);
                bindConstant()
                    .annotatedWith(Names.named("hikaricp.connectionTimeout"))
                    .to(connectionTimeoutMs);

                bindConstant()
                    .annotatedWith(Names.named("hikaricp.driverClassName"))
                    .to(driverClassName);

                bind(Properties.class)
                    .annotatedWith(Names.named("hikaricp.healthCheckProperties"))
                    .toInstance(healthCheckProperties);
                bind(Object.class)
                    .annotatedWith(Names.named("hikaricp.healthCheckRegistry"))
                    .toInstance(healthCheckRegistry);

                bindConstant().annotatedWith(Names.named("hikaricp.idleTimeout")).to(idleTimeoutMs);
                bindConstant()
                    .annotatedWith(Names.named("hikaricp.initializationFailTimeout"))
                    .to(initializationFailTimeout);
                bindConstant()
                    .annotatedWith(Names.named("hikaricp.isolateInternalQueries"))
                    .to(isolateInternalQueries);

                bindConstant()
                    .annotatedWith(Names.named("hikaricp.leakDetectionThreshold"))
                    .to(leakDetectionThresholdMs);

                bindConstant().annotatedWith(Names.named("hikaricp.maxLifetime")).to(maxLifetimeMs);
                bindConstant().annotatedWith(Names.named("hikaricp.maxPoolSize")).to(maxPoolSize);

                bind(MetricsTrackerFactory.class)
                    .annotatedWith(Names.named("hikaricp.metricsTrackerFactory"))
                    .toInstance(metricsTrackerFactory);

                bindConstant().annotatedWith(Names.named("hikaricp.minimumIdle")).to(minimumIdle);
                bindConstant().annotatedWith(Names.named("hikaricp.poolName")).to(poolName);
                bindConstant().annotatedWith(Names.named("hikaricp.readOnly")).to(readOnly);

                bindConstant().annotatedWith(Names.named("hikaricp.schema")).to(schema);

                bindConstant()
                    .annotatedWith(Names.named("hikaricp.validationTimeout"))
                    .to(validationTimeoutMs);
              }
            });

    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setDriverClassName(driverClassName);
    dataSourceConfig.setUrl(url);
    dataSourceConfig.setUsername(username);
    dataSourceConfig.setPassword(password);
    dataSourceConfig.setLoginTimeout(loginTimeout);
    HikariDataSourceWrapper dataSourceWrapper = new HikariDataSourceWrapper();
    dataSourceWrapper.setDataSourceConfig(dataSourceConfig);
    injector.injectMembers(dataSourceWrapper);
    dataSourceWrapper.init();

    HikariDataSource dataSource = dataSourceWrapper.getOrigDataSource();

    assertEquals(url, dataSource.getJdbcUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(1, dataSource.getLoginTimeout());

    assertEquals(allowPoolSuspension, dataSource.isAllowPoolSuspension());
    assertEquals(autoCommit, dataSource.isAutoCommit());

    assertEquals(catalog, dataSource.getCatalog());
    assertEquals(connectionInitSql, dataSource.getConnectionInitSql());
    assertEquals(connectionTestQuery, dataSource.getConnectionTestQuery());
    assertEquals(connectionTimeoutMs, dataSource.getConnectionTimeout());

    assertEquals(driverClassName, dataSource.getDriverClassName());

    assertEquals(healthCheckProperties, dataSource.getHealthCheckProperties());
    assertEquals(healthCheckRegistry, dataSource.getHealthCheckRegistry());

    assertEquals(idleTimeoutMs, dataSource.getIdleTimeout());
    assertEquals(initializationFailTimeout, dataSource.getInitializationFailTimeout());
    assertEquals(isolateInternalQueries, dataSource.isIsolateInternalQueries());

    assertEquals(leakDetectionThresholdMs, dataSource.getLeakDetectionThreshold());

    assertEquals(maxLifetimeMs, dataSource.getMaxLifetime());
    assertEquals(maxPoolSize, dataSource.getMaximumPoolSize());
    assertEquals(metricsTrackerFactory, dataSource.getMetricsTrackerFactory());
    assertEquals(minimumIdle, dataSource.getMinimumIdle());

    assertEquals(poolName, dataSource.getPoolName());
    assertEquals(readOnly, dataSource.isReadOnly());
    assertFalse(dataSource.isRegisterMbeans());
    assertEquals(schema, dataSource.getSchema());
    assertNull(dataSource.getTransactionIsolation());
    assertEquals(validationTimeoutMs, dataSource.getValidationTimeout());
  }
}
