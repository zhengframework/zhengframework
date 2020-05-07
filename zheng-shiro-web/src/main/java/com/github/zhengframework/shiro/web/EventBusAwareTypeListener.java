package com.github.zhengframework.shiro.web;

/*-
 * #%L
 * zheng-shiro-web
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import javax.inject.Provider;
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
