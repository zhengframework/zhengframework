package com.github.zhengframework.job;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class JobModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new JobModule();
  }
}
