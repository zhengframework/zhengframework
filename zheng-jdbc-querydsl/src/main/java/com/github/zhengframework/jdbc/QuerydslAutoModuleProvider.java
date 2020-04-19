package com.github.zhengframework.jdbc;

public class QuerydslAutoModuleProvider extends AbstractAutoModule {

  @Override
  protected void installModule(String name) {
    if (name.isEmpty()) {
      install(new QuerydslModule());
    } else {
      install(new QuerydslModule(name));
    }
  }

}
