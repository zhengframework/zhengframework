package com.github.zhengframework.jdbc.wrapper.druid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ExceptionSorter;
import com.github.zhengframework.jdbc.DataSourceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.Test;

public class DruidDataSourceWrapperTest {

  @Test
  public void get() throws Exception {
    final String driverClassName = "org.h2.Driver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";

    final int loginTimeout = 1; // Seconds

    final boolean autoCommit = true;
    final boolean readOnly = false;
    final Integer transactionIsolation = 10;
    final String catalog = "test";
    final int maxActive = 20;
    final int minIdle = 30;
    final int initialSize = 40;
    final long maxWait = 50;
    final boolean testOnBorrow = true;
    final boolean testOnReturn = true;
    final long timeBetweenEvictionRunsMillis = 60;
    final long minEvictableIdleTimeMillis = 30000;
    final boolean testWhileIdle = true;
    final String validationQuery = "SELECT 1";
    final int validationQueryTimeout = 80;
    final boolean accessToUnderlyingConnectionAllowed = true;
    final boolean removeAbandoned = true;
    final int removeAbandonedTimeout = 90;
    final boolean logAbandoned = true;
    final boolean poolPreparedStatements = true;
    final int maxOpenPreparedStatements = 100;
    final Properties connectProperties = new Properties();
    connectProperties.put("my_property", "true");
    final String filters = "com.alibaba.druid.filter.logging.Slf4jLogFilter";
    final String exceptionSorterClassName = TestExceptionSorter.class.getName();

    Injector injector =
        Guice.createInjector(
            new AbstractModule() {
              @Override
              protected void configure() {

                bindConstant().annotatedWith(Names.named("druid.defaultAutoCommit")).to(autoCommit);
                bindConstant().annotatedWith(Names.named("druid.defaultReadOnly")).to(readOnly);
                bindConstant()
                    .annotatedWith(Names.named("druid.defaultTransactionIsolation"))
                    .to(transactionIsolation);
                bindConstant().annotatedWith(Names.named("druid.defaultCatalog")).to(catalog);
                bindConstant().annotatedWith(Names.named("druid.maxActive")).to(maxActive);
                bindConstant().annotatedWith(Names.named("druid.minIdle")).to(minIdle);
                bindConstant().annotatedWith(Names.named("druid.initialSize")).to(initialSize);
                bindConstant().annotatedWith(Names.named("druid.maxWait")).to(maxWait);
                bindConstant().annotatedWith(Names.named("druid.testOnBorrow")).to(testOnBorrow);
                bindConstant().annotatedWith(Names.named("druid.testOnReturn")).to(testOnReturn);
                bindConstant()
                    .annotatedWith(Names.named("druid.timeBetweenEvictionRunsMillis"))
                    .to(timeBetweenEvictionRunsMillis);
                bindConstant()
                    .annotatedWith(Names.named("druid.minEvictableIdleTimeMillis"))
                    .to(minEvictableIdleTimeMillis);
                bindConstant().annotatedWith(Names.named("druid.testWhileIdle")).to(testWhileIdle);
                bindConstant()
                    .annotatedWith(Names.named("druid.validationQuery"))
                    .to(validationQuery);
                bindConstant()
                    .annotatedWith(Names.named("druid.validationQueryTimeout"))
                    .to(validationQueryTimeout);
                bindConstant()
                    .annotatedWith(Names.named("druid.accessToUnderlyingConnectionAllowed"))
                    .to(accessToUnderlyingConnectionAllowed);
                bindConstant()
                    .annotatedWith(Names.named("druid.removeAbandoned"))
                    .to(removeAbandoned);
                bindConstant()
                    .annotatedWith(Names.named("druid.removeAbandonedTimeout"))
                    .to(removeAbandonedTimeout);
                bindConstant().annotatedWith(Names.named("druid.logAbandoned")).to(logAbandoned);
                bindConstant()
                    .annotatedWith(Names.named("druid.poolPreparedStatements"))
                    .to(poolPreparedStatements);
                bindConstant()
                    .annotatedWith(Names.named("druid.maxOpenPreparedStatements"))
                    .to(maxOpenPreparedStatements);
                bind(Properties.class)
                    .annotatedWith(Names.named("druid.connectProperties"))
                    .toInstance(connectProperties);
                bindConstant().annotatedWith(Names.named("druid.filters")).to(filters);
                bindConstant()
                    .annotatedWith(Names.named("druid.exceptionSorterClassName"))
                    .to(exceptionSorterClassName);
              }
            });

    DataSourceConfig dataSourceConfig = new DataSourceConfig();
    dataSourceConfig.setDriverClassName(driverClassName);
    dataSourceConfig.setUrl(url);
    dataSourceConfig.setUsername(username);
    dataSourceConfig.setPassword(password);
    dataSourceConfig.setLoginTimeout(loginTimeout);
    DruidDataSourceWrapper dataSourceWrapper = new DruidDataSourceWrapper();
    dataSourceWrapper.setDataSourceConfig(dataSourceConfig);
    injector.injectMembers(dataSourceWrapper);
    dataSourceWrapper.init();

    DruidDataSource dataSource = dataSourceWrapper.getOrigDataSource();

    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(1, dataSource.getLoginTimeout());

    assertEquals(autoCommit, dataSource.isDefaultAutoCommit());
    assertEquals(readOnly, dataSource.getDefaultReadOnly());
    assertEquals(transactionIsolation, dataSource.getDefaultTransactionIsolation());
    assertEquals(catalog, dataSource.getDefaultCatalog());
    assertEquals(maxActive, dataSource.getMaxActive());
    assertEquals(minIdle, dataSource.getMinIdle());
    assertEquals(initialSize, dataSource.getInitialSize());
    assertEquals(maxWait, dataSource.getMaxWait());
    assertEquals(testOnBorrow, dataSource.isTestOnBorrow());
    assertEquals(testOnReturn, dataSource.isTestOnReturn());
    assertEquals(timeBetweenEvictionRunsMillis, dataSource.getTimeBetweenEvictionRunsMillis());
    assertEquals(minEvictableIdleTimeMillis, dataSource.getMinEvictableIdleTimeMillis());
    assertEquals(testWhileIdle, dataSource.isTestWhileIdle());
    assertEquals(validationQuery, dataSource.getValidationQuery());
    assertEquals(validationQueryTimeout, dataSource.getValidationQueryTimeout());
    assertEquals(
        accessToUnderlyingConnectionAllowed, dataSource.isAccessToUnderlyingConnectionAllowed());
    assertEquals(removeAbandoned, dataSource.isRemoveAbandoned());
    assertEquals(removeAbandonedTimeout, dataSource.getRemoveAbandonedTimeout());
    assertEquals(logAbandoned, dataSource.isLogAbandoned());
    assertEquals(poolPreparedStatements, dataSource.isPoolPreparedStatements());
    assertEquals(maxOpenPreparedStatements, dataSource.getMaxOpenPreparedStatements());
    assertEquals(connectProperties, dataSource.getConnectProperties());
    assertEquals(1, dataSource.getFilterClassNames().size());
    assertEquals(filters, dataSource.getFilterClassNames().get(0));
    assertTrue(dataSource.getExceptionSorter() instanceof TestExceptionSorter);
    assertEquals(exceptionSorterClassName, dataSource.getExceptionSorterClassName());
  }

  public static class TestExceptionSorter implements ExceptionSorter {

    @Override
    public boolean isExceptionFatal(SQLException e) {
      return false;
    }

    @Override
    public void configFromProperties(Properties properties) {
    }
  }
}
