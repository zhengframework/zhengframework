package com.github.zhengframework.log.logback;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class LogbackModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new LogbackModule();
  }
}
