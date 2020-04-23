package com.github.zhengframework.shiro;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.EventBusAware;

 class EventBusAwareTypeListener implements TypeListener {

   @Override
   public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {

     final Provider<EventBus> eventBusProvider = typeEncounter.getProvider(EventBus.class);

     if (EventBusAware.class.isAssignableFrom(typeLiteral.getRawType())) {
       typeEncounter.register(
           (InjectionListener<I>) o -> ((EventBusAware) o).setEventBus(eventBusProvider.get()));
     }
  }
}
