package com.dadazhishi.zheng.database;

import com.google.inject.AbstractModule;

public abstract class AbstractDataBaseModule extends AbstractModule {

  private final DataBaseConfig config;

  public AbstractDataBaseModule(DataBaseConfig config) {
    this.config = config;
  }

  public AbstractDataBaseModule() {
    config = null;
  }

  @Override
  protected void configure() {
    super.configure();
    if (config != null) {
      bind(DataBaseConfig.class).toInstance(config);
    }
  }

}
