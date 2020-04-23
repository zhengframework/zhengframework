package com.github.zhengframework.jdbc;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class JooqModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new JooqModule();
  }
}
