package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceProvider implements Provider<DataSource> {

  private HikariConfig config;
  private Provider<Injector> injectorProvider;
  private Annotation annotation;

  @Inject
  public DataSourceProvider(HikariConfig config,
      Provider<Injector> injectorProvider, Annotation annotation) {
    this.config = config;
    this.injectorProvider = injectorProvider;
    this.annotation = annotation;
  }

  @Override
  public DataSource get() {
    Injector injector = injectorProvider.get();
    Set<DataSourceProxy> dataSourceProxySet;
    if (annotation == null) {
      dataSourceProxySet = injector
          .getInstance(Key.get(new TypeLiteral<Set<DataSourceProxy>>() {
          }));
    } else {
      dataSourceProxySet = injector
          .getInstance(Key.get(new TypeLiteral<Set<DataSourceProxy>>() {
          }, annotation));
    }

    DataSource dataSource = new HikariDataSource(config);
    List<DataSourceProxy> collect = dataSourceProxySet.stream()
        .sorted(Comparator.comparing(DataSourceProxy::priority))
        .collect(Collectors.toList());
    for (DataSourceProxy dataSourceProxy : collect) {
      dataSource = dataSourceProxy.apply(dataSource);
    }
    ManagedSchema managedSchema;
    if (annotation == null) {
      managedSchema = injector.getInstance(Key.get(ManagedSchema.class));
    } else {
      managedSchema = injector.getInstance(Key.get(ManagedSchema.class, annotation));
    }
    log.info("managedSchema={}", managedSchema.getClass());
    managedSchema.migrate(dataSource);
    return dataSource;
  }
}
