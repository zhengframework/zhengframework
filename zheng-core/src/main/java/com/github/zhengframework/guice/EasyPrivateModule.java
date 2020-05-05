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

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;

public class EasyPrivateModule extends AbstractModule {

  public <T> void bindAndExpose(Class<T> superClass, Class<? extends Annotation> annotation,
      Class<? extends T> implementation, Consumer<Binder> requirements) {
    install(new LegModule<>(Key.get(superClass, annotation), implementation, requirements));
  }

  public <T> void bindAndExpose(Class<T> superClass, Annotation annotation,
      Class<? extends T> implementation, Consumer<Binder> requirements) {
    install(new LegModule<>(Key.get(superClass, annotation), implementation, requirements));
  }

  private static class LegModule<T> extends PrivateModule {

    private final Key<T> key;
    private final Class<? extends T> implementation;
    private final Consumer<Binder> requirements;

    LegModule(Key<T> key, Class<? extends T> implementation, Consumer<Binder> requirements) {
      this.key = key;
      this.implementation = implementation;
      this.requirements = requirements;
    }

    @Override
    protected void configure() {
      bind(key).to(implementation);
      expose(key);

      requirements.accept(binder());
    }
  }
}
