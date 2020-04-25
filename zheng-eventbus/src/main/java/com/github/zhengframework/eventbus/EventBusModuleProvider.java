package com.github.zhengframework.eventbus;

import com.github.zhengframework.core.ModuleProvider;
import com.google.inject.Module;

public class EventBusModuleProvider implements ModuleProvider {

  @Override
  public Module getModule() {
    return new EventBusModule();
  }
}
