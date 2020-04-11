package com.github.zhengframework.web;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class UndertowWebModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new UndertowWebModule();
  }
}
