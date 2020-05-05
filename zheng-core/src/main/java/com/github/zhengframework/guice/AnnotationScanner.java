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

import com.google.inject.Binding;
import com.google.inject.Injector;
import java.lang.annotation.Annotation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnnotationScanner<T> {

  private final Injector injector;
  private final Class<? extends Annotation> scanFor;

  public AnnotationScanner(Injector injector, Class<? extends Annotation> scanFor) {
    this.injector = injector;
    this.scanFor = scanFor;
  }

  /**
   * Start the process, visiting each ServletContextListener bound in the injector or any parents
   *
   * @param visitor Visitor
   */
  public void accept(Visitor<T> visitor) {
    accept(injector, visitor);
  }

  /**
   * Recursive impl that walks up the parent injectors first
   *
   * @param inj Injector
   * @param visitor Visitor
   */
  @SuppressWarnings("unchecked")
  private void accept(Injector inj, Visitor<T> visitor) {
    if (inj == null) {
      return;
    }

    accept(inj.getParent(), visitor);

    for (final Binding<?> binding : inj.getBindings().values()) {
      Class<?> rawType = binding.getKey().getTypeLiteral().getRawType();
      if (rawType != null) {
        if (rawType.isAnnotationPresent(scanFor)) {
          log.debug("{} rawType={}", scanFor, rawType);
          visitor.visit((T) binding.getProvider().get());
        }
      }
    }
  }

  public interface Visitor<V> {

    void visit(V thing);
  }
}
