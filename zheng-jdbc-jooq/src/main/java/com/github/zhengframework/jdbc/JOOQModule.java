package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import java.lang.annotation.Annotation;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;

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
    DSLContextProvider dslContextProvider = new DSLContextProvider(qualifier,
        getProvider(Injector.class));
    if (qualifier == null) {
      OptionalBinder.newOptionalBinder(binder(), Key.get(SQLDialect.class))
          .setDefault().toInstance(SQLDialect.DEFAULT);
      bind(Key.get(DSLContext.class)).toProvider(dslContextProvider);
    } else {
      OptionalBinder.newOptionalBinder(binder(), Key.get(SQLDialect.class, qualifier))
          .setDefault().toInstance(SQLDialect.DEFAULT);
      bind(Key.get(DSLContext.class, qualifier)).toProvider(dslContextProvider);
    }

  }
}
