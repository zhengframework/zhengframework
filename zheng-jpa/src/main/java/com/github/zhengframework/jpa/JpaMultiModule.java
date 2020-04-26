package com.github.zhengframework.jpa;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import java.util.Map;
import java.util.Map.Entry;

public class JpaMultiModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, JpaConfig> persistenceConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), JpaConfig.class);
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

}