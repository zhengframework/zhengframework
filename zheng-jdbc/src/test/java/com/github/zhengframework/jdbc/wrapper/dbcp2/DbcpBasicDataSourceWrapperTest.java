package com.github.zhengframework.jdbc.wrapper.dbcp2;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.jdbc.DataSourceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

public class DbcpBasicDataSourceWrapperTest {

  @Test
  public void get() throws Throwable {
    final String driverClassName = "org.h2.Driver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final boolean autoCommit = true;
    final Properties driverProperties = new Properties();
    driverProperties.put("my_property", "true");
    final boolean accessToUnderlyingConnectionAllowed = true;
    final String defaultCatalog = "test_catalog";
    final boolean defaultReadOnly = true;
    final int defaultTransactionIsolation = 20;
    final int initialSize = 30;
    final int maxTotal = 40;
    final int maxIdle = 50;
    final int maxOpenPreparedStatements = 60;
    final long maxWaitMillis = 70;
    final int minIdle = 80;
    final int numTestsPerEvictionRun = 90;
    final boolean poolPreparedStatements = true;
    final boolean testOnBorrow = true;
    final boolean testOnReturn = true;
    final boolean testWhileIdle = true;
    final long timeBetweenEvictionRunsMillis = 100;
    final String validationQuery = "SELECT 1";
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("dbcp.defaultAutoCommit")).to(autoCommit);
        bind(Properties.class).annotatedWith(Names.named("JDBC.driverProperties"))
            .toInstance(driverProperties);
        bindConstant().annotatedWith(Names.named("dbcp.accessToUnderlyingConnectionAllowed"))
            .to(accessToUnderlyingConnectionAllowed);
        bindConstant().annotatedWith(Names.named("dbcp.defaultCatalog")).to(defaultCatalog);
        bindConstant().annotatedWith(Names.named("dbcp.defaultReadOnly")).to(defaultReadOnly);
        bindConstant().annotatedWith(Names.named("dbcp.defaultTransactionIsolation"))
            .to(defaultTransactionIsolation);
        bindConstant().annotatedWith(Names.named("dbcp.initialSize")).to(initialSize);
        bindConstant().annotatedWith(Names.named("dbcp.maxTotal")).to(maxTotal);
        bindConstant().annotatedWith(Names.named("dbcp.maxIdle")).to(maxIdle);
        bindConstant().annotatedWith(Names.named("dbcp.maxOpenPreparedStatements"))
            .to(maxOpenPreparedStatements);
        bindConstant().annotatedWith(Names.named("dbcp.maxWaitMillis")).to(maxWaitMillis);
        bindConstant().annotatedWith(Names.named("dbcp.minIdle")).to(minIdle);
        bindConstant().annotatedWith(Names.named("dbcp.numTestsPerEvictionRun"))
            .to(numTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("dbcp.poolPreparedStatements"))
            .to(poolPreparedStatements);
        bindConstant().annotatedWith(Names.named("dbcp.testOnBorrow")).to(testOnBorrow);
        bindConstant().annotatedWith(Names.named("dbcp.testOnReturn")).to(testOnReturn);
        bindConstant().annotatedWith(Names.named("dbcp.testWhileIdle")).to(testWhileIdle);
        bindConstant().annotatedWith(Names.named("dbcp.timeBetweenEvictionRunsMillis"))
            .to(timeBetweenEvictionRunsMillis);
        bindConstant().annotatedWith(Names.named("dbcp.validationQuery")).to(validationQuery);
      }
    });
    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setDriverClassName(driverClassName);
    dataSourceConfig.setUrl(url);
    dataSourceConfig.setUsername(username);
    dataSourceConfig.setPassword(password);
    DbcpBasicDataSourceWrapper dataSourceWrapper = new DbcpBasicDataSourceWrapper();
    dataSourceWrapper.setDataSourceConfig(dataSourceConfig);
    injector.injectMembers(dataSourceWrapper);
    dataSourceWrapper.init();
    BasicDataSource dataSource = dataSourceWrapper.getOrigDataSource();

    assertEquals(driverClassName, dataSource.getDriverClassName());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.getDefaultAutoCommit());
    // Cannot test driver properties.
    assertEquals(accessToUnderlyingConnectionAllowed,
        dataSource.isAccessToUnderlyingConnectionAllowed());
    assertEquals(defaultCatalog, dataSource.getDefaultCatalog());
    assertEquals(defaultReadOnly, dataSource.getDefaultReadOnly());
    assertEquals(defaultTransactionIsolation, dataSource.getDefaultTransactionIsolation());
    assertEquals(initialSize, dataSource.getInitialSize());
    assertEquals(maxTotal, dataSource.getMaxTotal());
    assertEquals(maxIdle, dataSource.getMaxIdle());
    assertEquals(maxOpenPreparedStatements, dataSource.getMaxOpenPreparedStatements());
    assertEquals(maxWaitMillis, dataSource.getMaxWaitMillis());
    assertEquals(minIdle, dataSource.getMinIdle());
    assertEquals(numTestsPerEvictionRun, dataSource.getNumTestsPerEvictionRun());
    assertEquals(poolPreparedStatements, dataSource.isPoolPreparedStatements());
    assertEquals(testOnBorrow, dataSource.getTestOnBorrow());
    assertEquals(testOnReturn, dataSource.getTestOnReturn());
    assertEquals(testWhileIdle, dataSource.getTestWhileIdle());
    assertEquals(timeBetweenEvictionRunsMillis, dataSource.getTimeBetweenEvictionRunsMillis());
    assertEquals(validationQuery, dataSource.getValidationQuery());
  }

}