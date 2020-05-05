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
import com.google.inject.multibindings.MapBinderBinding;
import com.google.inject.multibindings.MultibinderBinding;
import com.google.inject.multibindings.MultibindingsTargetVisitor;
import com.google.inject.multibindings.OptionalBinderBinding;
import com.google.inject.spi.DefaultBindingTargetVisitor;
import com.google.inject.spi.LinkedKeyBinding;
import java.lang.reflect.Modifier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassScanner<T> {

  private final Injector injector;
  private final Class<T> scanFor;

  public ClassScanner(Injector injector, Class<T> scanFor) {
    this.injector = injector;
    this.scanFor = scanFor;
  }

  private static boolean isConcrete(Class<?> type) {
    return !type.isInterface() && !Modifier.isAbstract(type.getModifiers());
  }

  public static <T> Class<? extends T> getTargetClass(Binding<T> binding) {
    if (binding != null) {
      return binding.acceptTargetVisitor(new BindingInspectorVisitor<>());
    }
    return null;
  }

  /**
   * Start the process, visiting each ServletContextListener bound in the injector or any parents
   *
   * @param visitor Visitor
   */
  public void accept(Visitor<T> visitor) {
    accept(injector, visitor);
  }

  @SuppressWarnings("unchecked")
  private void accept(Injector inj, Visitor<T> visitor) {
    if (inj == null) {
      return;
    }

    accept(inj.getParent(), visitor);

    for (final Binding<?> binding : inj.getBindings().values()) {
      final Class<?> type = binding.getKey().getTypeLiteral().getRawType();
      if (type != null) {
        Class<?> targetType = getTargetClass(binding);
        if (targetType != null) {
          if (scanFor.isAssignableFrom(targetType)) {
            log.debug("{} targetType={}", scanFor, targetType);
            visitor.visit((T) binding.getProvider().get());
          }
        } else {
          if (scanFor.isAssignableFrom(type)) {
            log.debug("{} type={}", scanFor, type);
            visitor.visit((T) binding.getProvider().get());
          }
        }

      }
    }
  }

  public interface Visitor<V> {

    void visit(V thing);
  }

  private static final class BindingInspectorVisitor<T, C extends Class<? extends T>> extends
      DefaultBindingTargetVisitor<T, C> implements MultibindingsTargetVisitor<T, C> {

    @SuppressWarnings("unchecked")
    @Override
    public C visit(LinkedKeyBinding<? extends T> binding) {
      return (C) binding.getLinkedKey().getTypeLiteral().getRawType();
    }

    @Override
    public C visit(MultibinderBinding<? extends T> multibinderBinding) {
      return null;
    }

    @Override
    public C visit(MapBinderBinding<? extends T> mapBinderBinding) {
      return null;
    }

    @Override
    public C visit(OptionalBinderBinding<? extends T> optionalBinderBinding) {
      return null;
    }
  }
}
