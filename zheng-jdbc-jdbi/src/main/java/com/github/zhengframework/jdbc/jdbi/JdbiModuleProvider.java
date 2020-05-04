package com.github.zhengframework.jdbc.jdbi;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class JdbiModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new JdbiModule();
  }
}
