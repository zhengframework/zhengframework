package com.github.zhengframework.jdbc;

public class JooqAutoModuleProvider extends AbstractAutoModule {

  @Override
  protected void installModule(String name) {
    if (name.isEmpty()) {
      install(new JooqModule());
    } else {
      install(new JooqModule(name));
    }
  }

}
