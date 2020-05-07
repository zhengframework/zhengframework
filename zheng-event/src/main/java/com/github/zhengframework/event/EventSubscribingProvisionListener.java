package com.github.zhengframework.event;

/*-
 * #%L
 * zheng-event
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

import com.google.inject.spi.ProvisionListener;
import javax.inject.Provider;

class EventSubscribingProvisionListener implements ProvisionListener {

  private final Provider<EventDispatcher> dispatcherProvider;

  EventSubscribingProvisionListener(Provider<EventDispatcher> dispatcherProvider) {
    this.dispatcherProvider = dispatcherProvider;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> void onProvision(ProvisionInvocation<T> provision) {
    T provisioned = provision.provision();
    if (provisioned instanceof EventListener) {
      dispatcherProvider.get().registerListener((EventListener) provisioned);
    }
  }
}
