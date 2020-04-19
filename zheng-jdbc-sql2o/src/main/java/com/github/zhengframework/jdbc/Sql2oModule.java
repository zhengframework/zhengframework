package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import org.sql2o.Sql2o;

/**
 * https://github.com/aaberg/sql2o
 */
@EqualsAndHashCode(callSuper = false)
public class Sql2oModule extends AbstractModule {

  private Annotation qualifier;

  public Sql2oModule(Annotation qualifier) {
    this.qualifier = qualifier;
  }

  public Sql2oModule() {
    this.qualifier = null;
  }

  public Sql2oModule(String name) {
    this.qualifier = named(Objects.requireNonNull(name));
  }

  @Override
  protected void configure() {
    Sql2oProvider sql2oProvider = new Sql2oProvider(qualifier,
        getProvider(Injector.class));
    if (qualifier == null) {
      bind(Key.get(Sql2o.class)).toProvider(sql2oProvider);
    } else {
      bind(Key.get(Sql2o.class, qualifier)).toProvider(sql2oProvider);
    }
  }
}
