package com.github.zhengframework.web;

import com.github.zhengframework.service.ClassScanner;
import com.google.inject.Injector;
import java.util.EventListener;
import javax.inject.Inject;

public class EventListenerClassScanner extends ClassScanner<EventListener> {

  @Inject
  public EventListenerClassScanner(Injector injector) {
    super(injector, EventListener.class);
  }
}
