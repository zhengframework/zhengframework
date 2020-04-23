package com.github.zhengframework.jdbc;

import com.google.inject.Singleton;
import com.google.inject.persist.PersistModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import org.aopalliance.intercept.MethodInterceptor;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.BackslashEscaping;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;

public class JooqPersistModule extends PersistModule {

  private MethodInterceptor transactionInterceptor;

  @Override
  protected void configurePersistence() {

    bind(JooqPersistService.class).in(Singleton.class);
    bind(PersistService.class).to(JooqPersistService.class);
    bind(UnitOfWork.class).to(JooqPersistService.class);
    bind(DSLContext.class).toProvider(JooqPersistService.class);
    DefaultConfiguration configuration = new DefaultConfiguration();
    SQLDialect DIALECT = SQLDialect.DEFAULT;
    configuration.setSQLDialect(DIALECT);
    binder().bind(Configuration.class).toInstance(configuration);

    Settings settings = new Settings();
    BackslashEscaping ESCAPING = BackslashEscaping.OFF;
    settings.setBackslashEscaping(ESCAPING);
    binder().bind(Settings.class).toInstance(settings);

    transactionInterceptor = new JdbcLocalTxnInterceptor(getProvider(JooqPersistService.class),
        getProvider(UnitOfWork.class));
    requestInjection(transactionInterceptor);
  }

  @Override
  protected MethodInterceptor getTransactionInterceptor() {
    return transactionInterceptor;
  }
}
