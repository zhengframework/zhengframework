package com.github.zhengframework.metrics;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class MetricsModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new MetricsModule();
  }
}
