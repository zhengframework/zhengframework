package com.github.zhengframework.validator.aop;

/**
 * Callback used to execute logic under certain validation groups. Used from {@link
 * ValidationContext#doWithGroups(GroupAction, Class[])}.
 *
 * @param <T> return type
 * @author Vyacheslav Rusakov
 * @since 09.03.2016
 */
public interface GroupAction<T> {

  /**
   * @return result object (or null)
   * @throws Throwable in case of errors
   */
  T call() throws Throwable;
}
