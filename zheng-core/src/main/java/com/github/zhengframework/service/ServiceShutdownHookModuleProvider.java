package com.github.zhengframework.service;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class ServiceShutdownHookModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new ServiceShutdownHookModule();
  }
}
