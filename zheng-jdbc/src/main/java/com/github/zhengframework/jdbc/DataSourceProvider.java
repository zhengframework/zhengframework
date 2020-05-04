package com.github.zhengframework.jdbc;

import com.github.zhengframework.jdbc.wrapper.DataSourceWrapper;
import com.google.inject.Provider;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class DataSourceProvider implements Provider<DataSource> {

  private final Provider<Set<DataSourceProxy>> dataSourceProxySetProvider;
  private final Provider<ManagedSchema> managedSchemaProvider;
  private Provider<DataSourceWrapper> dataSourceWrapperProvider;
  private DataSource copyDataSource;
  private ReentrantLock lock = new ReentrantLock();

  @Inject
  public DataSourceProvider(
      Provider<DataSourceWrapper> dataSourceWrapperProvider,
      Provider<Set<DataSourceProxy>> dataSourceProxySetProvider,
      Provider<ManagedSchema> managedSchemaProvider) {
    this.dataSourceWrapperProvider = dataSourceWrapperProvider;
    this.dataSourceProxySetProvider = dataSourceProxySetProvider;
    this.managedSchemaProvider = managedSchemaProvider;
  }

  @Override
  public DataSource get() {
    lock.lock();
    if (copyDataSource == null) {
      List<DataSourceProxy> collect = dataSourceProxySetProvider.get().stream()
          .sorted(Comparator.comparing(DataSourceProxy::priority))
          .collect(Collectors.toList());
      copyDataSource = dataSourceWrapperProvider.get();
      for (DataSourceProxy dataSourceProxy : collect) {
        copyDataSource = dataSourceProxy.apply(copyDataSource);
      }
      managedSchemaProvider.get().migrate(copyDataSource);
    }
    lock.unlock();
    return copyDataSource;
  }

}
