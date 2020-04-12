package com.github.zhengframework.webjars;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class WebjarsModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new WebjarsModule();
  }
}
