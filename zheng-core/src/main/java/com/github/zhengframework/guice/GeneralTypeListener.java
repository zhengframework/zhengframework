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

import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.util.Locale;

public class GeneralTypeListener<T> implements TypeListener {

  private final Class<T> typeClass;
  private final TypePostProcessor<T> postProcessor;

  public GeneralTypeListener(final Class<T> typeClass, final TypePostProcessor<T> postProcessor) {
    this.typeClass = typeClass;
    this.postProcessor = postProcessor;
  }

  @SuppressWarnings("unchecked")
  public <I> void hear(final TypeLiteral<I> type, final TypeEncounter<I> encounter) {
    final Class<? super I> actualType = type.getRawType();
    if (!Utils.isPackageValid(actualType)) {
      return;
    }
    if (typeClass.isAssignableFrom(actualType)) {
      encounter.register(
          (InjectionListener<I>)
              injectee -> {
                try {
                  postProcessor.process((T) injectee);
                } catch (Exception ex) {
                  throw new IllegalStateException(
                      String.format(
                          Locale.ENGLISH,
                          "Failed to process type %s of class %s",
                          typeClass.getSimpleName(),
                          injectee.getClass().getSimpleName()),
                      ex);
                }
              });
    }
  }
}
