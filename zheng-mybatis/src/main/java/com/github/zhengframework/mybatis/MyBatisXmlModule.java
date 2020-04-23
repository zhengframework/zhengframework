package com.github.zhengframework.mybatis;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.guice.ExposedPrivateModule;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBatisXmlModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, MyBatisConfig> myBatisConfigMap = ConfigurationBeanMapper
        .resolve(configuration, MyBatisConfig.class);
    MyBatisConfig myBatisConfig = myBatisConfigMap.get("");
    install(new MyBatisXmlInternalModule(myBatisConfig));
    Class<? extends ExposedPrivateModule> extraModuleClass = myBatisConfig.getExtraModuleClass();
    if (extraModuleClass != null) {
      try {
        ExposedPrivateModule module = extraModuleClass.getDeclaredConstructor().newInstance();
        log.info("install module: " + extraModuleClass.getName());
        install(module);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
