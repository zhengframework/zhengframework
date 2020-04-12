package com.github.zhengframework.redis;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class RedisModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new RedisModule();
  }
}
