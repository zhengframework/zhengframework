package com.github.zhengframework.mybatis;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.guice.ExposedPrivateModule;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBatisXmlModule extends ConfigurationAwareModule {


  @Override
  protected void configure() {
    Map<String, MyBatisConfig> myBatisConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), MyBatisConfig.class);
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

}
