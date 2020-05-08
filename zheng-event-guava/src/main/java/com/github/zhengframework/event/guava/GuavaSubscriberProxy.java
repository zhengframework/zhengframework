package com.github.zhengframework.event.guava;

/*-
 * #%L
 * zheng-event-guava
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

import com.github.zhengframework.common.SuppressForbidden;
import com.github.zhengframework.event.Event;
import com.google.common.eventbus.Subscribe;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressForbidden
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
