package com.github.zhengframework.event.guava;

import com.github.zhengframework.event.Event;
import com.github.zhengframework.event.EventDispatcher;
import com.github.zhengframework.event.EventListener;
import com.github.zhengframework.event.EventRegistration;
import com.google.common.eventbus.EventBus;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import javax.inject.Inject;

@SuppressWarnings("UnstableApiUsage")
public class GuavaEventDispatcher implements EventDispatcher {

  private final Method eventListenerMethod;
  @SuppressWarnings("unused")
  @Inject
  private EventBus eventBus;

  public GuavaEventDispatcher() {
    try {
      this.eventListenerMethod = EventListener.class
          .getDeclaredMethod("onEvent", Event.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to cache EventListener method", e);
    }
  }

  public EventRegistration registerListener(Object instance, Method method,
      Class<? extends Event> eventType) {
    GuavaSubscriberProxy proxy = new GuavaSubscriberProxy(instance, method, eventType);
    Objects.requireNonNull(eventBus);
    eventBus.register(proxy);
    return new GuavaEventRegistration(eventBus, proxy);
  }

  public <T extends Event> EventRegistration registerListener(
      Class<T> eventType, EventListener<T> eventListener) {
    GuavaSubscriberProxy proxy = new GuavaSubscriberProxy(eventListener, eventListenerMethod,
        eventType);
    eventBus.register(proxy);
    return new GuavaEventRegistration(eventBus, proxy);
  }

  public EventRegistration registerListener(
      EventListener<? extends Event> eventListener) {
    Type[] genericInterfaces = eventListener.getClass().getGenericInterfaces();
    for (Type type : genericInterfaces) {
      if (EventListener.class.isAssignableFrom(TypeToken.of(type).getRawType())) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<?> rawType = TypeToken.of(parameterizedType.getActualTypeArguments()[0]).getRawType();
        GuavaSubscriberProxy proxy = new GuavaSubscriberProxy(eventListener, eventListenerMethod,
            rawType);
        eventBus.register(proxy);
        return new GuavaEventRegistration(eventBus, proxy);
      }
    }
    //no-op. Could not find anything to register.
    return () -> {
    };
  }

  @Override
  public void publishEvent(Event event) {
    eventBus.post(event);
  }
}
