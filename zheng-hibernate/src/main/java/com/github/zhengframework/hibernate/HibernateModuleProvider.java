package com.github.zhengframework.hibernate;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class HibernateModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new HibernateModule();
  }
}
