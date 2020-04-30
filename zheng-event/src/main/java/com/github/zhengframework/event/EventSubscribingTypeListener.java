package com.github.zhengframework.event;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class EventSubscribingTypeListener implements TypeListener {

  private final Provider<EventDispatcher> dispatcherProvider;

  EventSubscribingTypeListener(
      Provider<EventDispatcher> dispatcherProvider) {
    this.dispatcherProvider = dispatcherProvider;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
    final Class<?> clazz = type.getRawType();
    final List<Method> handlerMethods = getAllDeclaredHandlerMethods(clazz);
    if (!handlerMethods.isEmpty()) {
      encounter.register((InjectionListener<Object>) injectee -> {
        for (final Method handlerMethod : handlerMethods) {
          dispatcherProvider.get().registerListener(injectee, handlerMethod,
              (Class<? extends Event>) handlerMethod.getParameterTypes()[0]);
        }
      });
    }
  }

  private List<Method> getAllDeclaredHandlerMethods(Class<?> clazz) {
    final List<Method> handlerMethods = new ArrayList<>();
    while (clazz != null && !Collection.class.isAssignableFrom(clazz) && !clazz.isArray()) {
      for (final Method handlerMethod : clazz.getDeclaredMethods()) {
        if (handlerMethod.isAnnotationPresent(EventSubscribe.class)) {
          if (handlerMethod.getReturnType().equals(Void.TYPE)
              && handlerMethod.getParameterTypes().length == 1
              && Event.class.isAssignableFrom(handlerMethod.getParameterTypes()[0])) {
            handlerMethods.add(handlerMethod);
          } else {
            throw new IllegalArgumentException(
                "@EventSubscribe " + clazz.getName() + "." + handlerMethod.getName()
                    + "skipped. Methods must be public, void, and accept exactly"
                    + " one argument extending Event.");
          }
        }
      }
      clazz = clazz.getSuperclass();
    }
    return handlerMethods;
  }
}
