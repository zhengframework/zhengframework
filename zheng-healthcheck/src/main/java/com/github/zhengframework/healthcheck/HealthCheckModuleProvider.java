package com.github.zhengframework.healthcheck;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class HealthCheckModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new HealthCheckModule();
  }
}
