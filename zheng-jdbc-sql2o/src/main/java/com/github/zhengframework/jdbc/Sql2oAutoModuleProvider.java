package com.github.zhengframework.jdbc;

public class Sql2oAutoModuleProvider extends AbstractAutoModule {

  @Override
  protected void installModule(String name) {
    if (name.isEmpty()) {
      install(new Sql2oModule());
    } else {
      install(new Sql2oModule(name));
    }
  }

}
