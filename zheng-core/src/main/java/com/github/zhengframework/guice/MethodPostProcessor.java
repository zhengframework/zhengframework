package com.github.zhengframework.guice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface MethodPostProcessor<T extends Annotation> {

  void process(T annotation, Method method, Object instance) throws Exception;
}
