package com.github.zhengframework.shiro.jaxrs;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class ShiroJaxrsModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new ShiroJaxrsModule();
  }
}
