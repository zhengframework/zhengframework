package com.github.zhengframework.jpa;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import java.util.Map;
import java.util.Map.Entry;

public class JpaMultiModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, JpaConfig> persistenceConfigMap = ConfigurationBeanMapper
        .resolve(configuration, JpaConfig.class);
    for (Entry<String, JpaConfig> entry : persistenceConfigMap
        .entrySet()) {
      String name = entry.getKey();
      JpaConfig jpaConfig = entry.getValue();
      if (jpaConfig.getPersistenceUnitName() == null) {
        jpaConfig.setPersistenceUnitName("zheng-jpa-" + name);
      }
      install(new JpaPrivateModule(name.isEmpty() ? null : named(name), jpaConfig));
    }
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}