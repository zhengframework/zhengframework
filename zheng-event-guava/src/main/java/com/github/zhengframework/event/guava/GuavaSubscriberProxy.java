package com.github.zhengframework.event.guava;

import com.github.zhengframework.event.Event;
import com.google.common.eventbus.Subscribe;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GuavaSubscriberProxy {

  private final Object handlerInstance;
  private final Method handlerMethod;
  private final Class<?> acceptedType;

  public GuavaSubscriberProxy(Object handlerInstance, Method handlerMethod, Class<?> acceptedType) {
    this.handlerInstance = handlerInstance;
    this.handlerMethod = handlerMethod;
    this.acceptedType = acceptedType;
  }

  @Subscribe
  public void invokeEventHandler(Event event)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (acceptedType.isAssignableFrom(event.getClass())) {
      if (!handlerMethod.isAccessible()) {
        handlerMethod.setAccessible(true);
      }
      handlerMethod.invoke(handlerInstance, event);
    }
  }
}
