package com.dadazhishi.zheng.jdbc;

import static com.google.inject.name.Names.named;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.zaxxer.hikari.HikariConfig;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Singleton;
import javax.sql.DataSource;

public class DataSourceModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    DataSourceConfig dataSourceConfig = ConfigurationBeanMapper
        .resolve(configuration, DataSourceConfig.PREFIX, DataSourceConfig.class);
    if (!dataSourceConfig.isGroup()) {
      HikariConfig config = getHikariConfig(dataSourceConfig);
      bind(DataSource.class).toProvider(new DataSourceProvider(config)).in(Singleton.class);
    } else {
      Map<String, DataSourceConfig> dataSourceConfigMap = ConfigurationBeanMapper
          .resolveMap(configuration, DataSourceConfig.PREFIX, DataSourceConfig.class);
      for (Entry<String, DataSourceConfig> entry : dataSourceConfigMap
          .entrySet()) {
        String name = entry.getKey();
        DataSourceConfig dataSourceConfig1 = entry.getValue();
        HikariConfig config = getHikariConfig(dataSourceConfig1);
        bind(Key.get(DataSource.class, named(name))).toProvider(new DataSourceProvider(config))
            .in(Singleton.class);
      }
    }

  }

  private HikariConfig getHikariConfig(DataSourceConfig dataSourceConfig) {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(dataSourceConfig.getDriverClassName());
    config.setJdbcUrl(dataSourceConfig.getJdbcUrl());
    config.setUsername(dataSourceConfig.getUsername());
    config.setPassword(dataSourceConfig.getPassword());
    config.setAutoCommit(dataSourceConfig.isAutoCommit());
    config.setCatalog(dataSourceConfig.getCatalog());
    config.setConnectionInitSql(dataSourceConfig.getConnectionInitSql());
    config.setConnectionTestQuery(dataSourceConfig.getConnectionTestQuery());
    config.setConnectionTimeout(dataSourceConfig.getConnectionTimeout());
    config.setDataSourceClassName(dataSourceConfig.getDataSourceClassName());
    config.setIdleTimeout(dataSourceConfig.getIdleTimeout());
    config.setDataSourceJNDI(dataSourceConfig.getDataSourceJndiName());
    config.setInitializationFailTimeout(dataSourceConfig.getInitializationFailTimeout());
    config.setLeakDetectionThreshold(dataSourceConfig.getLeakDetectionThreshold());
    if (dataSourceConfig.getMaxPoolSize() > 1) {
      config.setMaximumPoolSize(dataSourceConfig.getMaxPoolSize());
    }
    if (dataSourceConfig.getMaxLifetime() > 1) {
      config.setMaxLifetime(dataSourceConfig.getMaxLifetime());
    }
    if (dataSourceConfig.getMinIdle() > 1) {
      config.setMinimumIdle(dataSourceConfig.getMinIdle());
    }
    config.setPoolName(dataSourceConfig.getPoolName());
    config.setReadOnly(dataSourceConfig.isReadOnly());
    config.setSchema(dataSourceConfig.getSchema());
    config.setValidationTimeout(dataSourceConfig.getValidationTimeout());
    for (Entry<String, String> entry : dataSourceConfig.getProperties().entrySet()) {
      config.addDataSourceProperty(entry.getKey(), entry.getValue());
    }
    return config;
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
