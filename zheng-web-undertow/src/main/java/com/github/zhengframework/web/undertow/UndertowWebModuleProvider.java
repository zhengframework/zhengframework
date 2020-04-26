package com.github.zhengframework.web.undertow;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class UndertowWebModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new UndertowWebModule();
  }
}
