package com.github.zhengframework.log;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class SLF4JBridgeModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new SLF4JBridgeModule();
  }
}
