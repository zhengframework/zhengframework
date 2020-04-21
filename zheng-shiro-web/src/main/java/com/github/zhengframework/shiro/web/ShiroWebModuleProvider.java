package com.github.zhengframework.shiro.web;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class ShiroWebModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new ShiroWebModule();
  }
}
