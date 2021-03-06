package com.github.zhengframework.guice;

/*-
 * #%L
 * zheng-core
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

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DestroyableManager {

  private final List<Destroyable> destroyListeners = new ArrayList<>();

  public DestroyableManager() {
    Runtime.getRuntime().addShutdownHook(new Thread(DestroyableManager.this::destroy));
  }

  public synchronized void register(final Destroyable destroyable) {
    destroyListeners.add(destroyable);
  }

  public void destroy() {
    synchronized (this) {
      for (Destroyable destroyable : destroyListeners) {
        try {
          destroyable.preDestroy();
        } catch (Exception ex) {
          log.error("Failed to properly destroy bean", ex);
        }
      }
      destroyListeners.clear();
    }
  }
}
