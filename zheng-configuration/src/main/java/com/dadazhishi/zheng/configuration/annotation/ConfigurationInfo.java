package com.github.zhengframework.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationInfo {

  /**
   * look like "zheng.web"
   */
  String prefix();

  /**
   * if true, will support group
   */
  boolean supportGroup() default false;

  ConfigurationExample[] examples() default {};
}
