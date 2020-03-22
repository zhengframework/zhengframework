package com.dadazhishi.zheng.datasource;

import com.google.inject.AbstractModule;

public abstract class AbstractDataSourceModule extends AbstractModule {

  public abstract void init(DataSourceConfig config);

}
