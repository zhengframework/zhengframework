package com.github.zhengframework.jpa;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import java.util.Map;

public class JpaModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, JpaConfig> persistenceConfigMap = ConfigurationBeanMapper
        .resolve(configuration, JpaConfig.class);
    JpaConfig jpaConfig = persistenceConfigMap.getOrDefault("", new JpaConfig());
    if (jpaConfig.getPersistenceUnitName() == null) {
      jpaConfig.setPersistenceUnitName("zheng-jpa");
    }
    install(new JpaInternalModule(jpaConfig));
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
