package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import org.jdbi.v3.core.Jdbi;

@EqualsAndHashCode(callSuper = false, of = {"qualifier"})
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
    JdbiProvider jdbiProvider = new JdbiProvider(qualifier);
    requestInjection(jdbiProvider);
    if (qualifier == null) {
      bind(Key.get(Jdbi.class)).toProvider(jdbiProvider);
    } else {
      bind(Key.get(Jdbi.class, qualifier)).toProvider(jdbiProvider);
    }
  }
}
