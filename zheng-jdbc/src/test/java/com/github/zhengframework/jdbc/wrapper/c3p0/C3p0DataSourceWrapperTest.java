package com.github.zhengframework.jdbc.wrapper.c3p0;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.jdbc.DataSourceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.ConnectionTester;
import java.sql.Connection;
import org.junit.Test;

public class C3p0DataSourceWrapperTest {

  @Test
  public void get() throws Exception {
    final String driverClassName = "org.h2.Driver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final boolean autoCommit = true;
    final int loginTimeout = 1; // Seconds
    final int acquireIncrement = 10;
    final int acquireRetryAttempts = 20;
    final int acquireRetryDelay = 30;
    final String automaticTestTable = "auto-table";
    final boolean breakAfterAcquireFailure = true;
    final int checkoutTimeout = 40;
    final String connectionTesterClassName = TestConnectionTester.class.getName();
    final int idleConnectionTestPeriod = 50;
    final int initialPoolSize = 60;
    final int maxAdministrativeTaskTime = 70;
    final int maxConnectionAge = 80;
    final int maxIdleTime = 90;
    final int maxIdleTimeExcessConnections = 100;
    final int maxPoolSize = 110;
    final int maxStatements = 120;
    final int maxStatementsPerConnection = 130;
    final int minPoolSize = 140;
    final String preferredTestQuery = "SELECT 1";
    final int propertyCycle = 150;
    final boolean testConnectionOnCheckin = true;
    final boolean testConnectionOnCheckout = true;
    final int unreturnedConnectionTimeout = 160;
    final boolean usesTraditionalReflectiveProxies = true;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.autoCommitOnClose")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("c3p0.acquireIncrement")).to(acquireIncrement);
        bindConstant().annotatedWith(Names.named("c3p0.acquireRetryAttempts"))
            .to(acquireRetryAttempts);
        bindConstant().annotatedWith(Names.named("c3p0.acquireRetryDelay")).to(acquireRetryDelay);
        bindConstant().annotatedWith(Names.named("c3p0.automaticTestTable")).to(automaticTestTable);
        bindConstant().annotatedWith(Names.named("c3p0.breakAfterAcquireFailure"))
            .to(breakAfterAcquireFailure);
        bindConstant().annotatedWith(Names.named("c3p0.checkoutTimeout")).to(checkoutTimeout);
        bindConstant().annotatedWith(Names.named("c3p0.connectionTesterClassName"))
            .to(connectionTesterClassName);
        bindConstant().annotatedWith(Names.named("c3p0.idleConnectionTestPeriod"))
            .to(idleConnectionTestPeriod);
        bindConstant().annotatedWith(Names.named("c3p0.initialPoolSize")).to(initialPoolSize);
        bindConstant().annotatedWith(Names.named("c3p0.maxAdministrativeTaskTime"))
            .to(maxAdministrativeTaskTime);
        bindConstant().annotatedWith(Names.named("c3p0.maxConnectionAge")).to(maxConnectionAge);
        bindConstant().annotatedWith(Names.named("c3p0.maxIdleTime")).to(maxIdleTime);
        bindConstant().annotatedWith(Names.named("c3p0.maxIdleTimeExcessConnections"))
            .to(maxIdleTimeExcessConnections);
        bindConstant().annotatedWith(Names.named("c3p0.maxPoolSize")).to(maxPoolSize);
        bindConstant().annotatedWith(Names.named("c3p0.maxStatements")).to(maxStatements);
        bindConstant().annotatedWith(Names.named("c3p0.maxStatementsPerConnection"))
            .to(maxStatementsPerConnection);
        bindConstant().annotatedWith(Names.named("c3p0.minPoolSize")).to(minPoolSize);
        bindConstant().annotatedWith(Names.named("c3p0.preferredTestQuery")).to(preferredTestQuery);
        bindConstant().annotatedWith(Names.named("c3p0.propertyCycle")).to(propertyCycle);
        bindConstant().annotatedWith(Names.named("c3p0.testConnectionOnCheckin"))
            .to(testConnectionOnCheckin);
        bindConstant().annotatedWith(Names.named("c3p0.testConnectionOnCheckout"))
            .to(testConnectionOnCheckout);
        bindConstant().annotatedWith(Names.named("c3p0.unreturnedConnectionTimeout"))
            .to(unreturnedConnectionTimeout);
        bindConstant().annotatedWith(Names.named("c3p0.usesTraditionalReflectiveProxies"))
            .to(usesTraditionalReflectiveProxies);
      }
    });

    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setDriverClassName(driverClassName);
    dataSourceConfig.setUrl(url);
    dataSourceConfig.setUsername(username);
    dataSourceConfig.setPassword(password);
    dataSourceConfig.setLoginTimeout(loginTimeout);
    C3p0DataSourceWrapper dataSourceWrapper = new C3p0DataSourceWrapper();
    dataSourceWrapper.setDataSourceConfig(dataSourceConfig);
    injector.injectMembers(dataSourceWrapper);
    dataSourceWrapper.init();
    ComboPooledDataSource dataSource = dataSourceWrapper.getOrigDataSource();

    assertEquals(driverClassName, dataSource.getDriverClass());
    assertEquals(url, dataSource.getJdbcUrl());
    assertEquals(username, dataSource.getUser());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isAutoCommitOnClose());
    assertEquals(acquireIncrement, dataSource.getAcquireIncrement());
    assertEquals(acquireRetryAttempts, dataSource.getAcquireRetryAttempts());
    assertEquals(acquireRetryDelay, dataSource.getAcquireRetryDelay());
    assertEquals(automaticTestTable, dataSource.getAutomaticTestTable());
    assertEquals(breakAfterAcquireFailure, dataSource.isBreakAfterAcquireFailure());
    assertEquals(checkoutTimeout, dataSource.getCheckoutTimeout());
    assertEquals(connectionTesterClassName, dataSource.getConnectionTesterClassName());
    assertEquals(idleConnectionTestPeriod, dataSource.getIdleConnectionTestPeriod());
    assertEquals(initialPoolSize, dataSource.getInitialPoolSize());
    assertEquals(maxAdministrativeTaskTime, dataSource.getMaxAdministrativeTaskTime());
    assertEquals(maxConnectionAge, dataSource.getMaxConnectionAge());
    assertEquals(maxIdleTime, dataSource.getMaxIdleTime());
    assertEquals(maxIdleTimeExcessConnections, dataSource.getMaxIdleTimeExcessConnections());
    assertEquals(maxPoolSize, dataSource.getMaxPoolSize());
    assertEquals(maxStatements, dataSource.getMaxStatements());
    assertEquals(maxStatementsPerConnection, dataSource.getMaxStatementsPerConnection());
    assertEquals(minPoolSize, dataSource.getMinPoolSize());
    assertEquals(preferredTestQuery, dataSource.getPreferredTestQuery());
    assertEquals(propertyCycle, dataSource.getPropertyCycle());
    assertEquals(testConnectionOnCheckin, dataSource.isTestConnectionOnCheckin());
    assertEquals(testConnectionOnCheckout, dataSource.isTestConnectionOnCheckout());
    assertEquals(unreturnedConnectionTimeout, dataSource.getUnreturnedConnectionTimeout());
    assertEquals(usesTraditionalReflectiveProxies, dataSource.isUsesTraditionalReflectiveProxies());
  }

  @SuppressWarnings("serial")
  public static final class TestConnectionTester implements ConnectionTester {

    @Override
    public int activeCheckConnection(Connection c) {
      return 0;
    }

    @Override
    public int statusOnException(Connection c, Throwable t) {
      return 0;
    }
  }
}