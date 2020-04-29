package com.github.zhengframework.validator.group;

import com.github.zhengframework.validator.ValidationGroups;
import com.github.zhengframework.validator.ValidatorConfig;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.groups.Default;

/**
 * Builds method validation groups context, by resolving {@link ValidationGroups} annotations.
 * Resolved contexts are cached for future re-use.
 * <p>
 * Default group is implicitly appended if allowed by module configuration (default true).
 *
 * @author Vyacheslav Rusakov
 * @since 09.03.2016
 */
@Singleton
public class MethodGroupsFactory {

  // lock will not affect performance for cached descriptors, just to make sure nothing was build two times
  private static final ReentrantLock LOCK = new ReentrantLock();

  private final Map<Method, Class<?>[]> cache = new HashMap<>();

  private ValidatorConfig validatorConfig;

  @Inject
  public MethodGroupsFactory(
      ValidatorConfig validatorConfig) {
    this.validatorConfig = validatorConfig;
  }

  public Class<?>[] create(final Method method) {
    Class<?>[] groups = cache.get(method);
    if (groups == null) {
      LOCK.lock();
      try {
        // groups could be created while thread wait for LOCK
        groups = cache.get(method);
        if (groups == null) {
          groups = buildGroups(method);
          cache.put(method, groups);
        }
      } finally {
        LOCK.unlock();
      }
    }
    return groups;
  }

  private Class<?>[] buildGroups(final Method method) {
    final List<ValidationGroups> annotations = GroupUtils.findAnnotations(method);
    // remove duplicates
    final Set<Class<?>> result = new LinkedHashSet<Class<?>>();
    for (ValidationGroups group : annotations) {
      Collections.addAll(result, group.value());
    }
    if (validatorConfig.isAddDefaultGroup()) {
      result.add(Default.class);
    }
    return result.toArray(new Class<?>[0]);
  }

  /**
   * Clears cached contexts (already parsed). Cache could be completely disabled using system
   * property or environment variable.
   */
  public void clearCache() {
    LOCK.lock();
    try {
      cache.clear();
    } finally {
      LOCK.unlock();
    }
  }

}
