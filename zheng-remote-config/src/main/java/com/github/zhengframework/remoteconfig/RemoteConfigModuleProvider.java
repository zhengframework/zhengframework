package com.github.zhengframework.remoteconfig;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class RemoteConfigModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new RemoteConfigModule();
  }
}
