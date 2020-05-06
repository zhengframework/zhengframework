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

import static com.github.zhengframework.guice.Utils.isPackageValid;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotatedMethodTypeListener<T extends Annotation> implements TypeListener {

  private final Class<T> annotationClass;
  private final MethodPostProcessor<T> postProcessor;

  public AnnotatedMethodTypeListener(
      final Class<T> annotationClass, final MethodPostProcessor<T> postProcessor) {
    this.annotationClass = annotationClass;
    this.postProcessor = postProcessor;
  }

  public <I> void hear(final TypeLiteral<I> type, final TypeEncounter<I> encounter) {
    final Class<? super I> actualType = type.getRawType();
    if (!isPackageValid(actualType)) {
      return;
    }
    Class<? super I> investigatingType = actualType;
    while (investigatingType != null && !investigatingType.equals(Object.class)) {
      for (final Method method : investigatingType.getDeclaredMethods()) {
        if (method.isAnnotationPresent(annotationClass)) {
          encounter.register(
              new InjectionListener<I>() {
                @Override
                public void afterInjection(I injected) {
                  try {
                    method.setAccessible(true);
                    postProcessor.process(method.getAnnotation(annotationClass), method, injected);
                  } catch (Exception ex) {
                    throw new IllegalStateException(
                        String.format(
                            "Failed to process annotation %s on method %s of class %s",
                            annotationClass.getSimpleName(),
                            method.getName(),
                            injected.getClass().getSimpleName()),
                        ex);
                  }
                }
              });
        }
      }
      investigatingType = investigatingType.getSuperclass();
    }
  }
}
