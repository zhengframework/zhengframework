package com.github.zhengframework.mybatis;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class MyBatisModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new MyBatisModule();
  }
}
