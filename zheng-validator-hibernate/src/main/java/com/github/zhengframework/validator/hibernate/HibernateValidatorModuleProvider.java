package com.github.zhengframework.validator.hibernate;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class HibernateValidatorModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new HibernateValidatorModule();
  }
}
