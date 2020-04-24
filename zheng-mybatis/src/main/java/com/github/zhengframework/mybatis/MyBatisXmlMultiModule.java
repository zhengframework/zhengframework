package com.github.zhengframework.mybatis;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import java.util.Map;
import java.util.Map.Entry;

public class MyBatisXmlMultiModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, MyBatisConfig> myBatisConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), MyBatisConfig.class);
    for (Entry<String, MyBatisConfig> entry : myBatisConfigMap.entrySet()) {
      String name = entry.getKey();
      MyBatisConfig myBatisConfig = entry.getValue();
      if (!name.isEmpty()) {
        install(new MyBatisXmlPrivateModule(named(name), myBatisConfig));
      } else {
        install(new MyBatisXmlPrivateModule(null, myBatisConfig));
      }
    }

  }

}
