package com.github.zhengframework.hibernate;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import java.util.Map;
import java.util.Map.Entry;

public class HibernateMultiModule extends AbstractModule implements ConfigurationAware {

  private com.github.zhengframework.configuration.Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, HibernateConfig> hibernateConfigMap = ConfigurationBeanMapper
        .resolve(configuration, HibernateConfig.class);

    for (Entry<String, HibernateConfig> entry : hibernateConfigMap
        .entrySet()) {
      String name = entry.getKey();
      HibernateConfig hibernateConfig = entry.getValue();
      install(new HibernatePrivateModule(name.isEmpty() ? null : named(name), hibernateConfig));
    }

  }

  @Override
  public void initConfiguration(
      com.github.zhengframework.configuration.Configuration configuration) {
    this.configuration = configuration;
  }

}
