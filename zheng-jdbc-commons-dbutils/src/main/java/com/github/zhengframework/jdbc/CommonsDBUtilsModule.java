package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.EqualsAndHashCode;
import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

@EqualsAndHashCode(callSuper = false)
public class CommonsDBUtilsModule extends AbstractModule {

  private Annotation qualifier;

  public CommonsDBUtilsModule(Annotation qualifier) {
    this.qualifier = qualifier;
  }

  public CommonsDBUtilsModule(String name) {
    this.qualifier = named(Objects.requireNonNull(name));
  }

  public CommonsDBUtilsModule() {
    qualifier = null;
  }

  @Override
  protected void configure() {
    QueryRunnerProvider queryRunnerProvider = new QueryRunnerProvider(qualifier,
        getProvider(Injector.class));
    AsyncQueryRunnerProvider asyncQueryRunnerProvider = new AsyncQueryRunnerProvider(qualifier,
        getProvider(Injector.class));
    if (qualifier == null) {
      OptionalBinder.newOptionalBinder(binder(), Key.get(ExecutorService.class))
          .setDefault().toInstance(Executors.newCachedThreadPool());
      bind(Key.get(QueryRunner.class)).toProvider(queryRunnerProvider);
      bind(Key.get(AsyncQueryRunner.class)).toProvider(asyncQueryRunnerProvider);
    } else {
      OptionalBinder.newOptionalBinder(binder(), Key.get(ExecutorService.class, qualifier))
          .setDefault().toInstance(Executors.newCachedThreadPool());
      bind(Key.get(QueryRunner.class, qualifier)).toProvider(queryRunnerProvider);
      bind(Key.get(AsyncQueryRunner.class, qualifier)).toProvider(asyncQueryRunnerProvider);
    }
  }
}
