package com.github.zhengframework.metrics.servlet;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class MetricsServletModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new MetricsServletModule();
  }
}
