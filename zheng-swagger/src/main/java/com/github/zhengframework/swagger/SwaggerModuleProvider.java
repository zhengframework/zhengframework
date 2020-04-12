package com.github.zhengframework.swagger;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class SwaggerModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new SwaggerModule();
  }
}
