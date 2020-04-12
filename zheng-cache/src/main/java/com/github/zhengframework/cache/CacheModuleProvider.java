package com.github.zhengframework.cache;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class CacheModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new CacheModule();
  }
}
