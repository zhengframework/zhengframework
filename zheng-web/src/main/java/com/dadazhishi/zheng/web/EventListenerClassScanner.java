package com.dadazhishi.zheng.web;

import com.dadazhishi.zheng.service.ClassScanner;
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
