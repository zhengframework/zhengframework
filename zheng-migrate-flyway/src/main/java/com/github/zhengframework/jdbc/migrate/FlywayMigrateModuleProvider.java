package com.github.zhengframework.jdbc.migrate;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class FlywayMigrateModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new FlywayMigrateModule();
  }
}
