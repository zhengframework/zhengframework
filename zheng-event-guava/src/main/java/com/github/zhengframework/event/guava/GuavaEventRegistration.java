package com.github.zhengframework.event.guava;

import com.github.zhengframework.event.EventRegistration;
import com.google.common.eventbus.EventBus;

@SuppressWarnings("UnstableApiUsage")
class GuavaEventRegistration implements EventRegistration {

  private final EventBus eventBus;
  private final GuavaSubscriberProxy subscriber;

  public GuavaEventRegistration(EventBus eventBus, GuavaSubscriberProxy subscriber) {
    this.eventBus = eventBus;
    this.subscriber = subscriber;
  }

  public void unregister() {
    this.eventBus.unregister(subscriber);
  }
}