package com.github.zhengframework.hibernate;

import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import java.util.Map;

/**
 * code base on [guice-persist-hibernate](https://github.com/jcampos8782/guice-persist-hibernate)
 */
public class HibernateModule extends AbstractModule implements ConfigurationAware {

  private com.github.zhengframework.configuration.Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, HibernateConfig> hibernateConfigMap = ConfigurationBeanMapper
        .resolve(configuration, HibernateConfig.class);
    HibernateConfig hibernateConfig = hibernateConfigMap.getOrDefault("", new HibernateConfig());
    install(new HibernateInternalModule(hibernateConfig));
  }

  @Override
  public void initConfiguration(
      com.github.zhengframework.configuration.Configuration configuration) {
    this.configuration = configuration;
  }

}
