package com.github.zhengframework.eventbus;

import com.google.common.eventbus.EventBus;
import javax.inject.Inject;

@SuppressWarnings("UnstableApiUsage")
public class Pub {

  @Inject
  private EventBus eventBus;

  public void publish() {
    eventBus.post(new PubEvent());
  }
}
