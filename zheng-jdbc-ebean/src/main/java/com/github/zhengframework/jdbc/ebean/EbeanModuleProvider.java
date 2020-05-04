package com.github.zhengframework.jdbc.ebean;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class EbeanModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new EbeanModule();
  }
}
