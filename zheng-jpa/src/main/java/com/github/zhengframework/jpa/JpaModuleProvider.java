package com.github.zhengframework.jpa;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class JpaModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new JpaModule();
  }
}
