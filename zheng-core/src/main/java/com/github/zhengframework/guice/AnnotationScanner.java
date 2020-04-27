package com.github.zhengframework.guice;

import com.google.inject.Binding;
import com.google.inject.Injector;
import java.lang.annotation.Annotation;
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
   *
   * @param visitor Visitor
   */
  public void accept(Visitor<T> visitor) {
    accept(injector, visitor);
  }

  /**
   * Recursive impl that walks up the parent injectors first
   *
   * @param inj Injector
   * @param visitor Visitor
   */
  @SuppressWarnings("unchecked")
  private void accept(Injector inj, Visitor<T> visitor) {
    if (inj == null) {
      return;
    }

    accept(inj.getParent(), visitor);

    for (final Binding<?> binding : inj.getBindings().values()) {
      Class<?> rawType = binding.getKey().getTypeLiteral().getRawType();
      if (rawType != null) {
        if (rawType.isAnnotationPresent(scanFor)) {
          log.debug("{} rawType={}", scanFor, rawType);
          visitor.visit((T) binding.getProvider().get());
        }
      }
    }
  }

  public interface Visitor<V> {

    void visit(V thing);
  }
}
