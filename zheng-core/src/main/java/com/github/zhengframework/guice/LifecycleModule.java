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
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class LifecycleModule extends AbstractModule {

  private Matcher<Object> typeMatcher;

  public LifecycleModule() {
    this(Matchers.any());
  }

  public LifecycleModule(final String pkg) {
    this(new ObjectPackageMatcher<>(pkg));
  }

  public LifecycleModule(final Matcher<Object> typeMatcher) {
    this.typeMatcher = typeMatcher;
  }

  @Override
  protected void configure() {
    DestroyableManager manager = new DestroyableManager();
    bind(DestroyableManager.class).toInstance(manager);

    bindListener(
        typeMatcher,
        new GeneralTypeListener<>(Destroyable.class, new DestroyableTypeProcessor(manager)));

    bindListener(
        typeMatcher,
        new AnnotatedMethodTypeListener<>(
            PostConstruct.class, new PostConstructAnnotationProcessor()));

    bindListener(
        typeMatcher,
        new AnnotatedMethodTypeListener<>(
            PreDestroy.class, new PreDestroyAnnotationProcessor(manager)));
  }
}
