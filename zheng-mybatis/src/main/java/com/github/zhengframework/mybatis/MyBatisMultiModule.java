package com.github.zhengframework.mybatis;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import java.util.Map;
import java.util.Map.Entry;

public class MyBatisMultiModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, MyBatisConfig> myBatisConfigMap = ConfigurationBeanMapper
        .resolve(configuration, MyBatisConfig.class);
    for (Entry<String, MyBatisConfig> entry : myBatisConfigMap.entrySet()) {
      String name = entry.getKey();
      MyBatisConfig myBatisConfig = entry.getValue();
      if (!name.isEmpty()) {
        install(new MyBatisPrivateModule(named(name), myBatisConfig));
      } else {
        install(new MyBatisPrivateModule(null, myBatisConfig));
      }
    }

  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
