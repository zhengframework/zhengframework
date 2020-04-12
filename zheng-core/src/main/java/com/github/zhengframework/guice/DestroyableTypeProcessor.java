package com.github.zhengframework.guice;

import javax.inject.Inject;

public class DestroyableTypeProcessor implements TypePostProcessor<Destroyable> {

  private final DestroyableManager manager;

  @Inject
  public DestroyableTypeProcessor(final DestroyableManager manager) {
    this.manager = manager;
  }

  @Override
  public void process(final Destroyable instance) {
    manager.register(instance);
  }
}
