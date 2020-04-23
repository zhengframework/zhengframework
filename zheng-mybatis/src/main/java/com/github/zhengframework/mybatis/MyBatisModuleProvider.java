package com.github.zhengframework.mybatis;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class MyBatisModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new MyBatisModule();
  }
}
