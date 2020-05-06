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

    List<Method> methods =
        ClassUtils.getAnnotatedMethods(typeLiteral.getRawType(), Subscribe.class);
    if (!methods.isEmpty()) {
      typeEncounter.register((InjectionListener<I>) o -> eventBusProvider.get().register(o));
    }
  }
}
