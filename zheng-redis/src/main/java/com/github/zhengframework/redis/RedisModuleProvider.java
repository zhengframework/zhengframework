package com.github.zhengframework.redis;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class RedisModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new RedisModule();
  }
}
