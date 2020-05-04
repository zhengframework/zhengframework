package com.github.zhengframework.jdbc.wrapper;

import com.github.zhengframework.jdbc.DataSourceConfig;
import com.google.inject.Injector;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceWrapperFactory {

  public DataSourceWrapper create(DataSourceConfig dataSourceConfig, Injector injector)
      throws Exception {
    Class<? extends DataSourceWrapper> dataSourceWrapperClass = dataSourceConfig
        .getDataSourceWrapperClass();
    log.info("dataSourceWrapperClass={}", dataSourceWrapperClass);
    DataSourceWrapper dataSourceWrapper = Objects
        .requireNonNull(dataSourceWrapperClass).getConstructor()
        .newInstance();
    dataSourceWrapper.setDataSourceConfig(dataSourceConfig);
    injector.injectMembers(dataSourceWrapper);
    dataSourceWrapper.init();
    return dataSourceWrapper;
  }
}
