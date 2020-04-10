package com.github.zhengframework.web;

import com.github.zhengframework.service.ClassScanner;
import com.google.inject.Injector;
import java.util.EventListener;

/**
 * Walks through the guice injector bindings, visiting each one that is an EventListener.
 */
public class EventListenerClassScanner extends ClassScanner<EventListener> {

  public EventListenerClassScanner(Injector injector) {
    super(injector, EventListener.class);
  }
}
