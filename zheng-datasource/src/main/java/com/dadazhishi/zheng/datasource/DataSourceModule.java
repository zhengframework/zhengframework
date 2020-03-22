package com.dadazhishi.zheng.datasource;

import com.google.inject.AbstractModule;
import java.util.ServiceLoader;

public abstract class DataSourceModule extends AbstractModule {

  protected void configure() {

    DataSourceConfig config = new DataSourceConfig();
    ServiceLoader<AbstractDataSourceModule> dataSourceModules = ServiceLoader
        .load(AbstractDataSourceModule.class);
    for (AbstractDataSourceModule dataSourceModule : dataSourceModules) {
      dataSourceModule.init(config);
      install(dataSourceModule);
    }

  }

}
