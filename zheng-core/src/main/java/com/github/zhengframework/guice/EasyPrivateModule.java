package com.github.zhengframework.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;

public class EasyPrivateModule extends AbstractModule {

  public <T> void bindAndExpose(Class<T> superClass, Class<? extends Annotation> annotation,
      Class<? extends T> implementation, Consumer<Binder> requirements) {
    install(new LegModule<>(Key.get(superClass, annotation), implementation, requirements));
  }

  public <T> void bindAndExpose(Class<T> superClass, Annotation annotation,
      Class<? extends T> implementation, Consumer<Binder> requirements) {
    install(new LegModule<>(Key.get(superClass, annotation), implementation, requirements));
  }

  private static class LegModule<T> extends PrivateModule {

    private final Key<T> key;
    private final Class<? extends T> implementation;
    private final Consumer<Binder> requirements;

    LegModule(Key<T> key, Class<? extends T> implementation, Consumer<Binder> requirements) {
      this.key = key;
      this.implementation = implementation;
      this.requirements = requirements;
    }

    @Override
    protected void configure() {
      bind(key).to(implementation);
      expose(key);

      requirements.accept(binder());
    }
  }
}
