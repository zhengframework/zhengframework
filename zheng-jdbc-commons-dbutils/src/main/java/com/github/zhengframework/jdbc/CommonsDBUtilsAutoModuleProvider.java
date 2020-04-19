package com.github.zhengframework.jdbc;

public class CommonsDBUtilsAutoModuleProvider extends AbstractAutoModule {

  @Override
  protected void installModule(String name) {
    if (name.isEmpty()) {
      install(new CommonsDBUtilsModule());
    } else {
      install(new CommonsDBUtilsModule(name));
    }
  }

}
