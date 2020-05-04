package com.github.zhengframework.jdbc.querydsl;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class QuerydslModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new QuerydslModule();
  }
}
