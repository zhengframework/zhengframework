package com.github.zhengframework.guice;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class LifecycleModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new LifecycleModule();
  }
}
