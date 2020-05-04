package com.github.zhengframework.shiro;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class ShiroModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new ShiroModule();
  }
}
