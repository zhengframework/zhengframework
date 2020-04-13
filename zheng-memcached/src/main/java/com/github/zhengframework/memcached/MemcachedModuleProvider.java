package com.github.zhengframework.memcached;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class MemcachedModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new MemcachedModule();
  }
}