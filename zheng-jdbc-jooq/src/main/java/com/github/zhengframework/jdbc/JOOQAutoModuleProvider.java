package com.github.zhengframework.jdbc;

public class JOOQAutoModuleProvider extends AbstractAutoModule {

  @Override
  protected void installModule(String name) {
    if (name.isEmpty()) {
      install(new JOOQModule());
    } else {
      install(new JOOQModule(name));
    }
  }

}
