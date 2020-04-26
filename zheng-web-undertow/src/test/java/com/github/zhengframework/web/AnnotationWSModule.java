package com.github.zhengframework.web;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

public class AnnotationWSModule extends AbstractModule {

  protected void configure() {
    Multibinder
        .newSetBinder(binder(), new TypeLiteral<Class<? extends WebSocketEndpoint>>() {
        }).addBinding().toInstance(EchoEndpoint.class);
  }
}
