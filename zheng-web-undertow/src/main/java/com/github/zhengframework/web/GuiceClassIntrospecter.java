package com.github.zhengframework.web;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.undertow.servlet.api.ClassIntrospecter;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.util.ImmediateInstanceFactory;

public class GuiceClassIntrospecter implements ClassIntrospecter {

  private final Injector injector;

  @Inject
  public GuiceClassIntrospecter(Injector injector) {
    this.injector = injector;
  }

  @Override
  public <T> InstanceFactory<T> createInstanceFactory(Class<T> clazz) {
    return new ImmediateInstanceFactory<>(injector.getInstance(clazz));
  }
}
