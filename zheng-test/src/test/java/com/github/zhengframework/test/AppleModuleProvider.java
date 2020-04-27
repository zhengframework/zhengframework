package com.github.zhengframework.test;

import com.github.zhengframework.core.ModuleProvider;
import com.github.zhengframework.test.Apple.Module;

public class AppleModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new Module();
  }
}
