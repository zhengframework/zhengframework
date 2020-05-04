package com.github.zhengframework.guice;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class LifecycleModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new LifecycleModule();
  }
}
