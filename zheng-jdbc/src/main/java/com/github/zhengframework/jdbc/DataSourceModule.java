package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Strings;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import com.zaxxer.hikari.HikariConfig;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class DataSourceModule extends ConfigurationAwareModule {


  @Override
  protected void configure() {
    Provider<Injector> injectorProvider = getProvider(Injector.class);
    Map<String, DataSourceConfig> dataSourceConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), DataSourceConfig.class);
    for (Entry<String, DataSourceConfig> entry : dataSourceConfigMap
        .entrySet()) {
      String name = entry.getKey();
      DataSourceConfig dataSourceConfig = entry.getValue();
      HikariConfig config = getHikariConfig(dataSourceConfig);
      if (name.isEmpty()) {
        bind(HikariConfig.class).toInstance(config);
        bind(DataSource.class).toProvider(new DataSourceProvider(config, injectorProvider,
            null))
            .in(Singleton.class);
        Multibinder.newSetBinder(binder(), DataSourceProxy.class)
            .addBinding().toInstance(dataSource -> dataSource);
        OptionalBinder.newOptionalBinder(binder(), ManagedSchema.class)
            .setDefault().toInstance(dataSource -> {
        });
      } else {
        bind(Key.get(HikariConfig.class, named(name))).toInstance(config);
        bind(Key.get(DataSource.class, named(name))).toProvider(new DataSourceProvider(config,
            injectorProvider, named(name)))
            .in(Singleton.class);
        Multibinder.newSetBinder(binder(), Key.get(DataSourceProxy.class, named(name)))
            .addBinding().toInstance(dataSource -> dataSource);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class, named(name)))
            .setDefault().toInstance(dataSource -> {
        });
      }

    }

  }

  private HikariConfig getHikariConfig(DataSourceConfig dataSourceConfig) {
    HikariConfig config = new HikariConfig();
    if (!Strings.isNullOrEmpty(dataSourceConfig.getDriverClassName())) {
      config.setDriverClassName(dataSourceConfig.getDriverClassName());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getJdbcUrl())) {
      config.setJdbcUrl(dataSourceConfig.getJdbcUrl());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getUsername())) {
      config.setUsername(dataSourceConfig.getUsername());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getPassword())) {
      config.setPassword(dataSourceConfig.getPassword());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getCatalog())) {
      config.setCatalog(dataSourceConfig.getCatalog());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getConnectionInitSql())) {
      config.setConnectionInitSql(dataSourceConfig.getConnectionInitSql());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getConnectionTestQuery())) {
      config.setConnectionTestQuery(dataSourceConfig.getConnectionTestQuery());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getDataSourceClassName())) {
      config.setDataSourceClassName(dataSourceConfig.getDataSourceClassName());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getDataSourceJndiName())) {
      config.setDataSourceJNDI(dataSourceConfig.getDataSourceJndiName());
    }
    config.setAutoCommit(dataSourceConfig.isAutoCommit());
    config.setConnectionTimeout(dataSourceConfig.getConnectionTimeout());
    config.setIdleTimeout(dataSourceConfig.getIdleTimeout());
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
    if (!Strings.isNullOrEmpty(dataSourceConfig.getPoolName())) {
      config.setPoolName(dataSourceConfig.getPoolName());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getSchema())) {
      config.setSchema(dataSourceConfig.getSchema());
    }
    if (!Strings.isNullOrEmpty(dataSourceConfig.getDataSourceJndiName())) {
      config.setDataSourceJNDI(dataSourceConfig.getDataSourceJndiName());
    }
    config.setReadOnly(dataSourceConfig.isReadOnly());
    config.setValidationTimeout(dataSourceConfig.getValidationTimeout());
    for (Entry<String, String> entry : dataSourceConfig.getProperties().entrySet()) {
      config.addDataSourceProperty(entry.getKey(), entry.getValue());
    }
    return config;
  }

}
