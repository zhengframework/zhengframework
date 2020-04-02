package com.dadazhishi.zheng.web;

import com.dadazhishi.zheng.service.Scanner;
import com.google.inject.Injector;
import java.util.EventListener;

/**
 * Walks through the guice injector bindings, visiting each one that is an EventListener.
 */
public class EventListenerScanner extends Scanner<EventListener> {

  public EventListenerScanner(Injector injector) {
    super(injector, EventListener.class);
  }
}
