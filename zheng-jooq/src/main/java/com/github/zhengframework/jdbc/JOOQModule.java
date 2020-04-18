package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import org.jooq.DSLContext;

@EqualsAndHashCode(callSuper = false)
public class JOOQModule extends AbstractModule {

  private Annotation qualifier;

  public JOOQModule() {
    this.qualifier = null;
  }

  public JOOQModule(Annotation qualifier) {
    this.qualifier = qualifier;
  }

  public JOOQModule(String name) {
    this.qualifier = named(Objects.requireNonNull(name));
  }

  @Override
  protected void configure() {
    DSLContextProvider dslContextProvider = new DSLContextProvider(qualifier);
    if (qualifier == null) {
      bind(Key.get(DSLContext.class)).toProvider(dslContextProvider);
    } else {
      bind(Key.get(DSLContext.class, qualifier)).toProvider(dslContextProvider);
    }

  }
}
