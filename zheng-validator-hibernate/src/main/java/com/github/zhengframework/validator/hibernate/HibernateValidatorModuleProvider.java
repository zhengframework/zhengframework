package com.github.zhengframework.validator.hibernate;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class HibernateValidatorModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new HibernateValidatorModule();
  }
}
