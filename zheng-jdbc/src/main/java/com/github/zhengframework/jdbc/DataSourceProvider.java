package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

public class DataSourceProvider implements Provider<DataSource> {

  private HikariConfig config;
  private Provider<Injector> injectorProvider;

  @Inject
  public DataSourceProvider(HikariConfig config,
      Provider<Injector> injectorProvider) {
    this.config = config;
    this.injectorProvider = injectorProvider;
  }

  @Override
  public DataSource get() {
    Injector injector = injectorProvider.get();
    Set<DataSourceProxy> dataSourceProxySet = injector
        .getInstance(Key.get(new TypeLiteral<Set<DataSourceProxy>>() {
        }));
    DataSource dataSource = new HikariDataSource(config);
    List<DataSourceProxy> collect = dataSourceProxySet.stream()
        .sorted(Comparator.comparing(DataSourceProxy::priority))
        .collect(Collectors.toList());
    for (DataSourceProxy dataSourceProxy : collect) {
      dataSource = dataSourceProxy.apply(dataSource);
    }
    ManagedSchema managedSchema = injector.getInstance(ManagedSchema.class);
    managedSchema.migrate(dataSource);
    return dataSource;
  }
}
