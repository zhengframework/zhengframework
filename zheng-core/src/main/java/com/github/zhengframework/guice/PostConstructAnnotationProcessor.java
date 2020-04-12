package com.github.zhengframework.guice;

import java.lang.reflect.Method;
import javax.annotation.PostConstruct;

public class PostConstructAnnotationProcessor implements MethodPostProcessor<PostConstruct> {

  @Override
  public void process(final PostConstruct annotation, final Method method, final Object instance) throws Exception {
    Utils.checkNoParams(method);
    method.invoke(instance);
  }
}
