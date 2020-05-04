package com.github.zhengframework.validator.bval;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class BvalValidatorModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new BvalValidatorModule();
  }
}
