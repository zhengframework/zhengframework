package com.github.zhengframework.event;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

public class EventModule extends AbstractModule {

  @Override
  protected void configure() {
    Provider<EventDispatcher> dispatcherProvider = getProvider(EventDispatcher.class);
    bindListener(Matchers.any(), new EventSubscribingTypeListener(dispatcherProvider));
    bindListener(Matchers.any(), new EventSubscribingProvisionListener(
        dispatcherProvider));
  }
}
