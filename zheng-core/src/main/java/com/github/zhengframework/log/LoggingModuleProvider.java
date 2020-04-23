package com.github.zhengframework.log;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class LoggingModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new LoggingModule();
  }
}
