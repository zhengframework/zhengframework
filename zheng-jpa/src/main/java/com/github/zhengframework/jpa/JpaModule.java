package com.github.zhengframework.jpa;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.guice.ExposedPrivateModule;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, JpaConfig> persistenceConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), JpaConfig.class);
    JpaConfig jpaConfig = persistenceConfigMap.getOrDefault("", new JpaConfig());
    if (jpaConfig.getPersistenceUnitName() == null) {
      jpaConfig.setPersistenceUnitName("zheng-jpa");
    }
    install(new JpaInternalModule(jpaConfig));

    Class<? extends ExposedPrivateModule> extraModuleClass = jpaConfig.getExtraModuleClass();
    if (extraModuleClass != null) {
      try {
        ExposedPrivateModule module = extraModuleClass
            .getDeclaredConstructor().newInstance();
        log.info("install extra module: " + extraModuleClass.getName());
        install(module);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
