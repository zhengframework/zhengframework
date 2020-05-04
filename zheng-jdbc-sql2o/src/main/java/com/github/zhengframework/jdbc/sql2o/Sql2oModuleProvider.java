package com.github.zhengframework.jdbc.sql2o;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class Sql2oModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new Sql2oModule();
  }
}
