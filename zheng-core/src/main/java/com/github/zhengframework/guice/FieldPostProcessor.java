package com.github.zhengframework.guice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface FieldPostProcessor<T extends Annotation> {

  void process(T annotation, Field field, Object instance) throws Exception;
}
