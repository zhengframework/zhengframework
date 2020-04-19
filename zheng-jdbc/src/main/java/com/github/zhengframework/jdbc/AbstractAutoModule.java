package com.github.zhengframework.jdbc;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.core.ModuleProvider;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractAutoModule extends AbstractModule implements ConfigurationAware,
    ModuleProvider {

  private Configuration configuration;

  @Override
  public Module getModule() {
    return this;
  }

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    Map<String, DataSourceConfig> dataSourceConfigMap = ConfigurationBeanMapper
        .resolve(configuration, DataSourceConfig.class);
    for (Entry<String, DataSourceConfig> entry : dataSourceConfigMap
        .entrySet()) {
      String name = entry.getKey();
      installModule(name);
    }
  }

  protected abstract void installModule(String name);

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
