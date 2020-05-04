package com.github.zhengframework.jdbc.jooq;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class JooqModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new JooqModule();
  }
}
