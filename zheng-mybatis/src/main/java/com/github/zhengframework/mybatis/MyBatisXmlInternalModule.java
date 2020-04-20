package com.github.zhengframework.mybatis;

import java.util.Objects;
import java.util.Properties;
import org.mybatis.guice.XMLMyBatisModule;

public class MyBatisXmlInternalModule extends XMLMyBatisModule {

  private final MyBatisConfig myBatisConfig;

  public MyBatisXmlInternalModule(MyBatisConfig myBatisConfig) {
    this.myBatisConfig = myBatisConfig;
  }

  @Override
  protected void initialize() {
    setClassPathResource(Objects.requireNonNull(myBatisConfig.getConfigFile()));
    Properties properties = new Properties();
    properties.putAll(myBatisConfig.getProperties());
    addProperties(properties);
  }
}
