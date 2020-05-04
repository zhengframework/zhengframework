package com.github.zhengframework.jdbc.wrapper;

import com.github.zhengframework.jdbc.DataSourceConfig;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import java.util.concurrent.locks.ReentrantLock;
import javax.inject.Singleton;

@Singleton
public class DataSourceWrapperProvider implements Provider<DataSourceWrapper> {

  private final Provider<DataSourceConfig> dataSourceConfigProvider;
  private final Provider<Injector> injectorProvider;
  private DataSourceWrapper dataSourceWrapper;
  private ReentrantLock lock = new ReentrantLock();

  @Inject
  public DataSourceWrapperProvider(
      Provider<DataSourceConfig> dataSourceConfigProvider,
      Provider<Injector> injectorProvider) {
    this.dataSourceConfigProvider = dataSourceConfigProvider;
    this.injectorProvider = injectorProvider;
  }

  @Override
  public DataSourceWrapper get() {
    lock.lock();
    if (dataSourceWrapper == null) {
      DataSourceConfig dataSourceConfig = dataSourceConfigProvider.get();
      Injector injector = injectorProvider.get();
      DataSourceWrapperFactory factory = new DataSourceWrapperFactory();
      try {
        dataSourceWrapper = factory.create(dataSourceConfig, injector);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    lock.unlock();
    return dataSourceWrapper;
  }
}
