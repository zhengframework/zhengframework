package com.github.zhengframework.shiro;

/*-
 * #%L
 * zheng-shiro
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

import com.github.zhengframework.guice.ClassScanner;
import com.github.zhengframework.service.Service;
import com.google.inject.Injector;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.Destroyable;

@Slf4j
public class ShiroService implements Service {

  private final Injector injector;

  @Inject
  public ShiroService(Injector injector) {
    this.injector = injector;
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  public void start() throws Exception {
  }

  @Override
  public void stop() throws Exception {
    ClassScanner<Destroyable> classScanner = new ClassScanner<>(injector, Destroyable.class);
    classScanner.accept(
        destroyable -> {
          try {
            destroyable.destroy();
          } catch (Exception e) {
            log.warn("Error destroying component class: " + destroyable.getClass(), e);
          }
        });
  }
}
