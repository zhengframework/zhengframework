package com.github.zhengframework.mybatis;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.guice.ExposedPrivateModule;
import com.google.inject.Key;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBatisModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, MyBatisConfig> myBatisConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), MyBatisConfig.class);
    MyBatisConfig myBatisConfig = myBatisConfigMap.get("");
    requireBinding(Key.get(DataSource.class));
    install(new MyBatisInternalModule(myBatisConfig));
    Class<? extends ExposedPrivateModule> extraModuleClass = myBatisConfig.getExtraModuleClass();
    if (extraModuleClass != null) {
      try {
        ExposedPrivateModule module = extraModuleClass.getDeclaredConstructor().newInstance();
        log.info("install extra module: " + extraModuleClass.getName());
        install(module);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
