package com.github.zhengframework.jdbc.wrapper.dbcp2;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.jdbc.DataSourceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.sql.Connection;
import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;
import org.junit.Test;

public class DbcpSharedDataSourceWrapperTest {

  @Test
  public void get() throws Throwable {
    final String driverClassName = "org.h2.Driver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final int loginTimeout = 1; // Seconds

    final boolean autoCommit = true;
    final boolean defaultReadOnly = true;
    final int defaultTransactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
    final String description = "test_description";
    final int defaultMinEvictableIdleTimeMillis = 30;
    final int defaultNumTestsPerEvictionRun = 40;
    final boolean rollbackAfterValidation = true;
    final boolean defaultTestOnBorrow = true;
    final boolean defaultTestOnReturn = true;
    final boolean defaultTestWhileIdle = true;
    final long defaultTimeBetweenEvictionRunsMillis = 50;
    final String validationQuery = "SELECT 1";
    final int defaultMaxTotal = 60;
    final int defaultMaxIdle = 70;
    final int defaultMaxWaitMillis = 80;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("dbcp.defaultAutoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("dbcp.defaultReadOnly")).to(defaultReadOnly);
        bindConstant().annotatedWith(Names.named("dbcp.defaultTransactionIsolation"))
            .to(defaultTransactionIsolation);
        bindConstant().annotatedWith(Names.named("dbcp.description")).to(description);
        bindConstant().annotatedWith(Names.named("dbcp.defaultMinEvictableIdleTimeMillis"))
            .to(defaultMinEvictableIdleTimeMillis);
        bindConstant().annotatedWith(Names.named("dbcp.defaultNumTestsPerEvictionRun"))
            .to(defaultNumTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("dbcp.rollbackAfterValidation"))
            .to(rollbackAfterValidation);
        bindConstant().annotatedWith(Names.named("dbcp.defaultTestOnBorrow"))
            .to(defaultTestOnBorrow);
        bindConstant().annotatedWith(Names.named("dbcp.defaultTestOnReturn"))
            .to(defaultTestOnReturn);
        bindConstant().annotatedWith(Names.named("dbcp.defaultTestWhileIdle"))
            .to(defaultTestWhileIdle);
        bindConstant().annotatedWith(Names.named("dbcp.defaultTimeBetweenEvictionRunsMillis"))
            .to(defaultTimeBetweenEvictionRunsMillis);
        bindConstant().annotatedWith(Names.named("dbcp.validationQuery")).to(validationQuery);
        bindConstant().annotatedWith(Names.named("dbcp.defaultMaxTotal")).to(defaultMaxTotal);
        bindConstant().annotatedWith(Names.named("dbcp.defaultMaxIdle")).to(defaultMaxIdle);
        bindConstant().annotatedWith(Names.named("dbcp.defaultMaxWaitMillis"))
            .to(defaultMaxWaitMillis);
      }
    });
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setDriverClassName(driverClassName);
    dataSourceConfig.setUrl(url);
    dataSourceConfig.setUsername(username);
    dataSourceConfig.setPassword(password);
    dataSourceConfig.setLoginTimeout(loginTimeout);
    DbcpSharedDataSourceWrapper dataSourceWrapper = new DbcpSharedDataSourceWrapper();
    dataSourceWrapper.setDataSourceConfig(dataSourceConfig);
    injector.injectMembers(dataSourceWrapper);
    dataSourceWrapper.init();
    SharedPoolDataSource dataSource = dataSourceWrapper.getOrigDataSource();

    assertEquals(autoCommit, dataSource.isDefaultAutoCommit());
    assertEquals(defaultReadOnly, dataSource.isDefaultReadOnly());
    assertEquals(defaultTransactionIsolation, dataSource.getDefaultTransactionIsolation());
    assertEquals(description, dataSource.getDescription());
    assertEquals(loginTimeout, dataSource.getLoginTimeout());
    assertEquals(defaultMinEvictableIdleTimeMillis,
        dataSource.getDefaultMinEvictableIdleTimeMillis());
    assertEquals(defaultNumTestsPerEvictionRun, dataSource.getDefaultNumTestsPerEvictionRun());
    assertEquals(rollbackAfterValidation, dataSource.isRollbackAfterValidation());
    assertEquals(defaultTestOnBorrow, dataSource.getDefaultTestOnBorrow());
    assertEquals(defaultTestOnReturn, dataSource.getDefaultTestOnReturn());
    assertEquals(defaultTestWhileIdle, dataSource.getDefaultTestWhileIdle());
    assertEquals(defaultTimeBetweenEvictionRunsMillis,
        dataSource.getDefaultTimeBetweenEvictionRunsMillis());
    assertEquals(validationQuery, dataSource.getValidationQuery());
    assertEquals(defaultMaxTotal, dataSource.getDefaultMaxTotal());
    assertEquals(defaultMaxIdle, dataSource.getDefaultMaxIdle());
    assertEquals(defaultMaxWaitMillis, dataSource.getDefaultMaxWaitMillis());
  }
}