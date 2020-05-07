package com.github.zhengframework.jdbc.wrapper.c3p0;

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
import com.google.inject.Inject;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import javax.inject.Named;
import javax.sql.ConnectionPoolDataSource;

public class C3p0DataSourceWrapper extends DataSourceWrapper {

  private ComboPooledDataSource origDataSource = new ComboPooledDataSource();

  @Inject(optional = true)
  public void setDescription(@Named("c3p0.description") String description) {
    origDataSource.setDescription(description);
  }

  @Inject(optional = true)
  public void setForceUseNamedDriverClass(
      @Named("c3p0.forceUseNamedDriverClass") boolean forceUseNamedDriverClass) {
    origDataSource.setForceUseNamedDriverClass(forceUseNamedDriverClass);
  }

  public ComboPooledDataSource getOrigDataSource() {
    return origDataSource;
  }

  /**
   * Sets the acquire increment.
   *
   * @param acquireIncrement the new acquire increment.
   */
  @Inject(optional = true)
  public void setAcquireIncrement(@Named("c3p0.acquireIncrement") final int acquireIncrement) {
    origDataSource.setAcquireIncrement(acquireIncrement);
  }

  /**
   * Sets the acquire retry attempts.
   *
   * @param acquireRetryAttempts the new acquire retry attempts.
   */
  @Inject(optional = true)
  public void setAcquireRetryAttempts(
      @Named("c3p0.acquireRetryAttempts") final int acquireRetryAttempts) {
    origDataSource.setAcquireRetryAttempts(acquireRetryAttempts);
  }

  /**
   * Sets the acquire retry delay.
   *
   * @param acquireRetryDelay the new acquire retry delay
   */
  @Inject(optional = true)
  public void setAcquireRetryDelay(@Named("c3p0.acquireRetryDelay") final int acquireRetryDelay) {
    origDataSource.setAcquireRetryDelay(acquireRetryDelay);
  }

  /**
   * Sets the auto commit on close.
   *
   * @param autoCommit the new auto commit on close
   */
  @Inject(optional = true)
  public void setAutoCommitOnClose(@Named("JDBC.autoCommitOnClose") final boolean autoCommit) {
    origDataSource.setAutoCommitOnClose(autoCommit);
  }

  @Inject(optional = true)
  public void setContextClassLoaderSource(
      @Named("c3p0.contextClassLoaderSource") String contextClassLoaderSource)
      throws PropertyVetoException {
    origDataSource.setContextClassLoaderSource(contextClassLoaderSource);
  }

  /**
   * Sets the driver properties.
   *
   * @param driverProperties the new driver properties
   */
  @Inject(optional = true)
  public void setDriverProperties(
      @Named("JDBC.driverProperties") final Properties driverProperties) {
    origDataSource.setProperties(driverProperties);
  }

  /**
   * Sets the aautomatic test table.
   *
   * @param automaticTestTable the new aautomatic test table
   */
  @Inject(optional = true)
  public void setAutomaticTestTable(
      @Named("c3p0.automaticTestTable") final String automaticTestTable) {
    origDataSource.setAutomaticTestTable(automaticTestTable);
  }

  @Inject(optional = true)
  public void setForceIgnoreUnresolvedTransactions(
      @Named("c3p0.forceIgnoreUnresolvedTransactions") boolean forceIgnoreUnresolvedTransactions) {
    origDataSource.setForceIgnoreUnresolvedTransactions(forceIgnoreUnresolvedTransactions);
  }

  @Inject(optional = true)
  public void setPrivilegeSpawnedThreads(
      @Named("c3p0.privilegeSpawnedThreads") boolean privilegeSpawnedThreads) {
    origDataSource.setPrivilegeSpawnedThreads(privilegeSpawnedThreads);
  }

  /**
   * Sets the break after acquire failure.
   *
   * @param breakAfterAcquireFailure the new break after acquire failure.
   */
  @Inject(optional = true)
  public void setBreakAfterAcquireFailure(
      @Named("c3p0.breakAfterAcquireFailure") final boolean breakAfterAcquireFailure) {
    origDataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
  }

  /**
   * Sets the checkout timeout.
   *
   * @param checkoutTimeout the new checkout timeout.
   */
  @Inject(optional = true)
  public void setCheckoutTimeout(@Named("c3p0.checkoutTimeout") final int checkoutTimeout) {
    origDataSource.setCheckoutTimeout(checkoutTimeout);
  }

  /**
   * Sets the connection customizer class name.
   *
   * @param connectionCustomizerClassName the new connection customizer class name.
   */
  @Inject(optional = true)
  public void setConnectionCustomizerClassName(
      @Named("c3p0.connectionCustomizerClassName") final String connectionCustomizerClassName) {
    origDataSource.setConnectionCustomizerClassName(connectionCustomizerClassName);
  }

  /**
   * Sets the connection tester class name.
   *
   * @param connectionTesterClassName the new connection tester class name.
   */
  @Inject(optional = true)
  public void setConnectionTesterClassName(
      @Named("c3p0.connectionTesterClassName") final String connectionTesterClassName) {
    try {
      origDataSource.setConnectionTesterClassName(connectionTesterClassName);
    } catch (PropertyVetoException e) {
      throw new RuntimeException(
          "Impossible to set C3P0 Data Source connection tester class name '"
              + connectionTesterClassName
              + "', see nested exceptions",
          e);
    }
  }

  /**
   * Sets the idle connection test period.
   *
   * @param idleConnectionTestPeriod the new idle connection test period
   */
  @Inject(optional = true)
  public void setIdleConnectionTestPeriod(
      @Named("c3p0.idleConnectionTestPeriod") final int idleConnectionTestPeriod) {
    origDataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
  }

  /**
   * Sets the initial pool size.
   *
   * @param initialPoolSize the new initial pool size
   */
  @Inject(optional = true)
  public void setInitialPoolSize(@Named("c3p0.initialPoolSize") final int initialPoolSize) {
    origDataSource.setInitialPoolSize(initialPoolSize);
  }

  /**
   * Sets the max administrative task time.
   *
   * @param maxAdministrativeTaskTime the new max administrative task time.
   */
  @Inject(optional = true)
  public void setMaxAdministrativeTaskTime(
      @Named("c3p0.maxAdministrativeTaskTime") final int maxAdministrativeTaskTime) {
    origDataSource.setMaxAdministrativeTaskTime(maxAdministrativeTaskTime);
  }

  /**
   * Sets the max connection age.
   *
   * @param maxConnectionAge the new max connection age.
   */
  @Inject(optional = true)
  public void setMaxConnectionAge(@Named("c3p0.maxConnectionAge") final int maxConnectionAge) {
    origDataSource.setMaxConnectionAge(maxConnectionAge);
  }

  /**
   * Sets the max idle time.
   *
   * @param maxIdleTime the new max idle time.
   */
  @Inject(optional = true)
  public void setMaxIdleTime(@Named("c3p0.maxIdleTime") final int maxIdleTime) {
    origDataSource.setMaxIdleTime(maxIdleTime);
  }

  /**
   * Sets the max idle time excess connections.
   *
   * @param maxIdleTimeExcessConnections the new max idle time excess connections.
   */
  @Inject(optional = true)
  public void setMaxIdleTimeExcessConnections(
      @Named("c3p0.maxIdleTimeExcessConnections") final int maxIdleTimeExcessConnections) {
    origDataSource.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
  }

  /**
   * Sets the max pool size.
   *
   * @param maxPoolSize the new max pool size.
   */
  @Inject(optional = true)
  public void setMaxPoolSize(@Named("c3p0.maxPoolSize") final int maxPoolSize) {
    origDataSource.setMaxPoolSize(maxPoolSize);
  }

  /**
   * Sets the max statements.
   *
   * @param maxStatements the new max statements.
   */
  @Inject(optional = true)
  public void setMaxStatements(@Named("c3p0.maxStatements") final int maxStatements) {
    origDataSource.setMaxStatements(maxStatements);
  }

  /**
   * Sets the max statements per connection.
   *
   * @param maxStatementsPerConnection the new max statements per connection.
   */
  @Inject(optional = true)
  public void setMaxStatementsPerConnection(
      @Named("c3p0.maxStatementsPerConnection") final int maxStatementsPerConnection) {
    origDataSource.setMaxStatementsPerConnection(maxStatementsPerConnection);
  }

  /**
   * Sets the min pool size.
   *
   * @param minPoolSize the new min pool size.
   */
  @Inject(optional = true)
  public void setMinPoolSize(@Named("c3p0.minPoolSize") final int minPoolSize) {
    origDataSource.setMinPoolSize(minPoolSize);
  }

  @Inject(optional = true)
  public void setOverrideDefaultUser(
      @Named("c3p0.overrideDefaultUser") String overrideDefaultUser) {
    origDataSource.setOverrideDefaultUser(overrideDefaultUser);
  }

  @Inject(optional = true)
  public void setOverrideDefaultPassword(
      @Named("c3p0.overrideDefaultPassword") String overrideDefaultPassword) {
    origDataSource.setOverrideDefaultPassword(overrideDefaultPassword);
  }

  /**
   * Sets the preferred test query.
   *
   * @param preferredTestQuery the new preferred test query.
   */
  @Inject(optional = true)
  public void setPreferredTestQuery(
      @Named("c3p0.preferredTestQuery") final String preferredTestQuery) {
    origDataSource.setPreferredTestQuery(preferredTestQuery);
  }

  /**
   * Sets the property cycle.
   *
   * @param propertyCycle the new property cycle.
   */
  @Inject(optional = true)
  public void setPropertyCycle(@Named("c3p0.propertyCycle") final int propertyCycle) {
    origDataSource.setPropertyCycle(propertyCycle);
  }

  /**
   * Sets the test connection on checkin.
   *
   * @param testConnectionOnCheckin the new test connection on checkin.
   */
  @Inject(optional = true)
  public void setTestConnectionOnCheckin(
      @Named("c3p0.testConnectionOnCheckin") final boolean testConnectionOnCheckin) {
    origDataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
  }

  /**
   * Sets the test connection on checkout.
   *
   * @param testConnectionOnCheckout the new test connection on checkout.
   */
  @Inject(optional = true)
  public void setTestConnectionOnCheckout(
      @Named("c3p0.testConnectionOnCheckout") final boolean testConnectionOnCheckout) {
    origDataSource.setTestConnectionOnCheckout(testConnectionOnCheckout);
  }

  /**
   * Sets the unreturned connection timeout.
   *
   * @param unreturnedConnectionTimeout the new unreturned connection timeout
   */
  @Inject(optional = true)
  public void setUnreturnedConnectionTimeout(
      @Named("c3p0.unreturnedConnectionTimeout") final int unreturnedConnectionTimeout) {
    origDataSource.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
  }

  @Inject(optional = true)
  public void setUserOverridesAsString(@Named("c3p0.userOverridesAsString") String uoas)
      throws PropertyVetoException {
    origDataSource.setUserOverridesAsString(uoas);
  }

  @Inject(optional = true)
  public void setDebugUnreturnedConnectionStackTraces(
      @Named("c3p0.debugUnreturnedConnectionStackTraces")
          boolean debugUnreturnedConnectionStackTraces) {
    origDataSource.setDebugUnreturnedConnectionStackTraces(debugUnreturnedConnectionStackTraces);
  }

  @Inject(optional = true)
  public void setForceSynchronousCheckins(
      @Named("c3p0.forceSynchronousCheckins") boolean forceSynchronousCheckins) {
    origDataSource.setForceSynchronousCheckins(forceSynchronousCheckins);
  }

  @Inject(optional = true)
  public void setStatementCacheNumDeferredCloseThreads(
      @Named("c3p0.statementCacheNumDeferredCloseThreads")
          int statementCacheNumDeferredCloseThreads) {
    origDataSource.setStatementCacheNumDeferredCloseThreads(statementCacheNumDeferredCloseThreads);
  }

  @Inject(optional = true)
  public void setFactoryClassLocation(
      @Named("c3p0.factoryClassLocation") String factoryClassLocation) {
    origDataSource.setFactoryClassLocation(factoryClassLocation);
  }

  @Inject(optional = true)
  public void setConnectionPoolDataSource(
      @Named("c3p0.connectionPoolDataSource") ConnectionPoolDataSource connectionPoolDataSource)
      throws PropertyVetoException {
    origDataSource.setConnectionPoolDataSource(connectionPoolDataSource);
  }

  @Inject(optional = true)
  public void setDataSourceName(@Named("c3p0.dataSourceName") String dataSourceName) {
    origDataSource.setDataSourceName(dataSourceName);
  }

  @SuppressWarnings("rawtypes")
  @Inject(optional = true)
  public void setExtensions(@Named("c3p0.extensions") Map extensions) {
    origDataSource.setExtensions(extensions);
  }

  @Inject(optional = true)
  public void setIdentityToken(@Named("c3p0.identityToken") String identityToken) {
    origDataSource.setIdentityToken(identityToken);
  }

  @Inject(optional = true)
  public void setNumHelperThreads(@Named("c3p0.numHelperThreads") int numHelperThreads) {
    origDataSource.setNumHelperThreads(numHelperThreads);
  }

  /**
   * Sets the uses traditional reflective proxies.
   *
   * @param usesTraditionalReflectiveProxies the new uses traditional reflective proxies.
   */
  @Inject(optional = true)
  public void setUsesTraditionalReflectiveProxies(
      @Named("c3p0.usesTraditionalReflectiveProxies")
          final boolean usesTraditionalReflectiveProxies) {
    origDataSource.setUsesTraditionalReflectiveProxies(usesTraditionalReflectiveProxies);
  }

  @Override
  public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
    super.setDataSourceConfig(dataSourceConfig);
    origDataSource.setJdbcUrl(Objects.requireNonNull(dataSourceConfig.getUrl()));
    origDataSource.setUser(dataSourceConfig.getUsername());
    origDataSource.setPassword(dataSourceConfig.getPassword());
    Map<String, String> properties = dataSourceConfig.getProperties();
    PropertyHelper.setTargetFromProperties(this, properties);
  }

  @Override
  public void init() throws Exception {
    DataSourceConfig dataSourceConfig = getDataSourceConfig();
    origDataSource.setDriverClass(Objects.requireNonNull(dataSourceConfig.getDriverClassName()));
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
