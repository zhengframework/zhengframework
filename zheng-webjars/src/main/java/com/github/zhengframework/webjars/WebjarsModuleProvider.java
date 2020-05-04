package com.github.zhengframework.webjars;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class WebjarsModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new WebjarsModule();
  }
}
