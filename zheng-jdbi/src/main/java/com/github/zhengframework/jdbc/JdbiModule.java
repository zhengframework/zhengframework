package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@EqualsAndHashCode(callSuper = false)
public class JdbiModule extends AbstractModule {

  private Annotation qualifier;

  public JdbiModule(Annotation qualifier) {
    this.qualifier = qualifier;
  }

  public JdbiModule(String name) {
    this.qualifier = named(Objects.requireNonNull(name));
  }


  @Override
  protected void configure() {
    JdbiProvider jdbiProvider = new JdbiProvider(qualifier, getProvider(Injector.class));
    requestInjection(jdbiProvider);
    if (qualifier == null) {
      OptionalBinder.newOptionalBinder(binder(), Key.get(new TypeLiteral<List<JdbiPlugin>>() {
      }))
          .setDefault().toInstance(Collections.singletonList(new SqlObjectPlugin()));
      bind(Key.get(Jdbi.class)).toProvider(jdbiProvider);
    } else {
      OptionalBinder.newOptionalBinder(binder(), Key.get(new TypeLiteral<List<JdbiPlugin>>() {
      }, qualifier))
          .setDefault().toInstance(Collections.singletonList(new SqlObjectPlugin()));
      bind(Key.get(Jdbi.class, qualifier)).toProvider(jdbiProvider);
    }
  }
}
