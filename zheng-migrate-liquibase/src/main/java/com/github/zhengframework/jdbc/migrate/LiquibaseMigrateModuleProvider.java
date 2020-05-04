package com.github.zhengframework.jdbc.migrate;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class LiquibaseMigrateModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new LiquibaseMigrateModule();
  }
}
