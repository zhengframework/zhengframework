package com.github.zhengframework.remoteconfig.servlet;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class RemoteConfigServletModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new RemoteConfigServletModule();
  }
}
