package com.github.zhengframework.cache;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;

public class JCacheModule extends AbstractModule {

  private final Module module;

  public JCacheModule(Module module) {
    this.module = module;
  }

  @Override
  protected void configure() {
    install(Modules.override(new CacheAnnotationsModule()).with(module));
  }
}
