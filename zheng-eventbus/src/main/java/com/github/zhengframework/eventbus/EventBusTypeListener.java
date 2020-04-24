package com.github.zhengframework.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

@SuppressWarnings("UnstableApiUsage")
public class EventBusTypeListener implements TypeListener {

  private EventBus eventBus;

  public EventBusTypeListener(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
    typeEncounter.register((InjectionListener<I>) eventBus::register);
  }
}
