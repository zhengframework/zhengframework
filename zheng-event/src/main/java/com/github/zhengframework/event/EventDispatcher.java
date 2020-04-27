package com.github.zhengframework.event;

import java.lang.reflect.Method;

@SuppressWarnings("UnusedReturnValue")
public interface EventDispatcher {

  <T extends Event> EventRegistration registerListener(
      Class<T> eventType, EventListener<T> eventListener);

  EventRegistration registerListener(EventListener<? extends Event> eventListener);

  EventRegistration registerListener(Object instance, Method method,
      Class<? extends Event> acceptedType);

  void publishEvent(Event event);
}
