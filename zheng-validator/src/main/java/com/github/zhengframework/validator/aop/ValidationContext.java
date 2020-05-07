package com.github.zhengframework.validator.aop;

/*-
 * #%L
 * zheng-validator
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

import com.github.zhengframework.validator.ValidationGroups;
import com.google.common.base.Throwables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Singleton;
import javax.validation.groups.Default;

/**
 * Defines validation groups used by method validation. Actual groups are defined with {@link
 * ValidationGroups} annotation.
 *
 * <p>Context is thread bound.
 *
 * <p>Inline contexts inherit all groups from upper levels.
 *
 * <p>Default group is implicitly appended if allowed by module configuration (default true).
 *
 * <p>Groups may be defined directly (without annotations usage) by using {@link
 * ValidationContext#doWithGroups(GroupAction, Class[])}.
 *
 * @author Vyacheslav Rusakov
 * @since 07.03.2016
 */
@Singleton
public class ValidationContext {

  private static final Class<?>[] EMPTY = new Class<?>[0];
  private final ThreadLocal<List<Class<?>[]>> threadContext = new ThreadLocal<List<Class<?>[]>>();

  /** @return current context validation groups or empty array when no groups defined */
  public Class<?>[] getContextGroups() {
    Class<?>[] res = EMPTY;
    final List<Class<?>[]> context = threadContext.get();
    if (context != null) {
      res = context.get(context.size() - 1);
    }
    return res;
  }

  /**
   * Defines context validation groups. Context is defined for all logic inside action callback (in
   * current thread). Note: does not override current context groups.
   *
   * @param action action callback to be executed with validation groups
   * @param groups validation groups to use
   * @param <T> action return type
   * @return object produced by action callback
   */
  public <T> T doWithGroups(final GroupAction<T> action, final Class<?>... groups) {
    pushContext(groups);
    try {
      return action.call();
    } catch (Throwable ex) {
      Throwables.throwIfUnchecked(ex);
      throw new IllegalStateException(ex);
    } finally {
      popContext();
    }
  }

  private void pushContext(final Class<?>[] groups) {
    List<Class<?>[]> context = threadContext.get();
    if (context == null) {
      context = new ArrayList<>();
      threadContext.set(context);
    }
    // remove duplicates
    final Set<Class<?>> allGroups = new LinkedHashSet<Class<?>>();
    if (!context.isEmpty()) {
      Collections.addAll(allGroups, context.get(context.size() - 1));
      // default group will always be last (here it comes from upper context and must be removed)
      allGroups.remove(Default.class);
    }
    Collections.addAll(allGroups, groups);
    context.add(allGroups.toArray(new Class<?>[0]));
  }

  private void popContext() {
    final List<Class<?>[]> context = threadContext.get();
    if (context.size() > 1) {
      context.remove(context.size() - 1);
    } else {
      threadContext.remove();
    }
  }
}
