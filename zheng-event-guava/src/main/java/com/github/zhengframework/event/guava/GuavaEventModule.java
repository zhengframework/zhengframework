package com.github.zhengframework.event.guava;

import com.github.zhengframework.event.EventDispatcher;
import com.github.zhengframework.event.EventModule;
import com.google.inject.AbstractModule;

public class GuavaEventModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new EventModule());
    bind(EventDispatcher.class).to(GuavaEventDispatcher.class).asEagerSingleton();
  }

}
