package com.github.zhengframework.event.guava;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class GuavaEventModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new GuavaEventModule();
  }
}
