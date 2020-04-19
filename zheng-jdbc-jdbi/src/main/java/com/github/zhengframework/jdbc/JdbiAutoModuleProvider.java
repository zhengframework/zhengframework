package com.github.zhengframework.jdbc;

public class JdbiAutoModuleProvider extends AbstractAutoModule {

  @Override
  protected void installModule(String name) {
    if (name.isEmpty()) {
      install(new JdbiModule());
    } else {
      install(new JdbiModule(name));
    }
  }

}
