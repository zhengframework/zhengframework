package com.github.zhengframework.validator.bval;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class BvalValidatorModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new BvalValidatorModule();
  }
}
