package com.github.zhengframework.cache;

import com.google.inject.AbstractModule;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;

public class CacheModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new CacheAnnotationsModule());
  }
}
