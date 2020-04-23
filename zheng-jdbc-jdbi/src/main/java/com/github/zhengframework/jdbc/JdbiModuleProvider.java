package com.github.zhengframework.jdbc;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class JdbiModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new JdbiModule();
  }
}
