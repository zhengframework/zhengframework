package com.dadazhishi.zheng.service;

import com.google.inject.Binding;
import com.google.inject.Injector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnnotationScanner<T> {

  private final Injector injector;
  private final Class<? extends Annotation> scanFor;

  public AnnotationScanner(Injector injector, Class<? extends Annotation> scanFor) {
    this.injector = injector;
    this.scanFor = scanFor;
  }

  /**
   * Start the process, visiting each ServletContextListener bound in the injector or any parents
   */
  public void accept(Visitor<T> visitor) {
    accept(injector, visitor);
  }

  /**
   * Recursive impl that walks up the parent injectors first
   */
  private void accept(Injector inj, Visitor visitor) {
    if (inj == null) {
      return;
    }

    accept(inj.getParent(), visitor);

    for (final Binding<?> binding : inj.getBindings().values()) {
      final Type type = binding.getKey().getTypeLiteral().getType();
      Class<?> rawType = binding.getKey().getTypeLiteral().getRawType();
      if (rawType.isAnnotationPresent(scanFor)) {
        log.info("{} rawType={}", scanFor, rawType);
        visitor.visit(binding.getProvider().get());
      }
    }
  }

  public interface Visitor<V> {

    void visit(V thing);
  }
}
