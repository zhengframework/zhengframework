package com.github.zhengframework.jdbc.sql2o;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class Sql2oModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new Sql2oModule();
  }
}
