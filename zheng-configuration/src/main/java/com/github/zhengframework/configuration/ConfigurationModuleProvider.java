package com.github.zhengframework.configuration;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class ConfigurationModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new ConfigurationModule();
  }
}
