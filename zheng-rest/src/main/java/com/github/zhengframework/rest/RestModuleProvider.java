package com.github.zhengframework.rest;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class RestModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new RestModule();
  }
}
