package com.github.zhengframework.jdbc;

import com.github.zhengframework.jdbc.wrapper.DataSourceWrapper;
import com.github.zhengframework.service.Service;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DataSourceService implements Service {

  private Provider<DataSourceWrapper> dataSourceWrapperProvider;

  @Inject
  public DataSourceService(Provider<DataSourceWrapper> dataSourceWrapperProvider) {
    this.dataSourceWrapperProvider = dataSourceWrapperProvider;
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  public void start() throws Exception {

  }

  @Override
  public void stop() throws Exception {
    dataSourceWrapperProvider.get().close();
  }
}
