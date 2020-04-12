package com.github.zhengframework.guice;

import java.lang.reflect.Method;

public class AnnotatedMethodDestroyable implements Destroyable {

  private final Method method;
  private final Object instance;

  public AnnotatedMethodDestroyable(final Method method, final Object instance) {
    this.method = method;
    this.instance = instance;
  }

  @Override
  public void preDestroy() throws Exception {
    if(instance!=null){
      method.invoke(instance);
    }
  }
}
