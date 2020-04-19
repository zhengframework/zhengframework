package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DSLContextProvider implements Provider<DSLContext> {

  private final Annotation qualifier;

  private final Provider<Injector> injectorProvider;

  public DSLContextProvider(Annotation qualifier,
      Provider<Injector> injectorProvider) {
    this.qualifier = qualifier;
    this.injectorProvider = injectorProvider;
  }

  @Override
  public DSLContext get() {
    DataSource dataSource;
    SQLDialect sqlDialect;
    Injector injector = injectorProvider.get();
    if (qualifier == null) {
      dataSource = injector.getInstance(Key.get(DataSource.class));
      sqlDialect = injector.getInstance(Key.get(SQLDialect.class));
    } else {
      dataSource = injector
          .getInstance(Key.get(DataSource.class, qualifier));
      sqlDialect = injector.getInstance(Key.get(SQLDialect.class, qualifier));
    }
    return DSL.using(dataSource, sqlDialect);
  }
}
