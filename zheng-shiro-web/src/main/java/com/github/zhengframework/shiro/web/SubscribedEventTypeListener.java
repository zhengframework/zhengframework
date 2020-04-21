package com.github.zhengframework.shiro.web;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.Subscribe;
import org.apache.shiro.util.ClassUtils;

class SubscribedEventTypeListener implements TypeListener {

  @Override
  public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {

    final Provider<EventBus> eventBusProvider = typeEncounter.getProvider(EventBus.class);

    List<Method> methods = ClassUtils
        .getAnnotatedMethods(typeLiteral.getRawType(), Subscribe.class);
    if (!methods.isEmpty()) {
      typeEncounter.register((InjectionListener<I>) o -> eventBusProvider.get().register(o));
    }
  }
}
