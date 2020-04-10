package com.github.zhengframework.configuration.annotation;

import com.github.zhengframework.configuration.spi.ConfigurationDefine;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationExample {

  /**
   * look like "prod"
   */
  String groupName() default "";

  Class<? extends ConfigurationDefine> example();
}
