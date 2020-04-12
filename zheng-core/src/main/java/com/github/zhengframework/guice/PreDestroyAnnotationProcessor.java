package com.github.zhengframework.guice;

import java.lang.reflect.Method;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

public class PreDestroyAnnotationProcessor implements MethodPostProcessor<PreDestroy> {

  private final DestroyableManager manager;

  @Inject
  public PreDestroyAnnotationProcessor(final DestroyableManager manager) {
    this.manager = manager;
  }

  @Override
  public void process(final PreDestroy annotation, final Method method, final Object instance) throws Exception {
    Utils.checkNoParams(method);
    manager.register(new AnnotatedMethodDestroyable(method, instance));
  }

}
