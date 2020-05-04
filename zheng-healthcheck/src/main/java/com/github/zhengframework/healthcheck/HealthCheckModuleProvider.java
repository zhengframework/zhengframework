package com.github.zhengframework.healthcheck;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class HealthCheckModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new HealthCheckModule();
  }
}
