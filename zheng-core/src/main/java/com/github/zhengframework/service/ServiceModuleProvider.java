package com.github.zhengframework.service;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class ServiceModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new ServiceModule();
  }
}
