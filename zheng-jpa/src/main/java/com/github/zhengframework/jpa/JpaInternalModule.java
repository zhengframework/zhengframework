package com.github.zhengframework.jpa;

import com.github.zhengframework.jpa.JpaService.EntityManagerFactoryInternalProvider;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import org.aopalliance.intercept.MethodInterceptor;

public class JpaInternalModule extends AbstractModule {

  private final JpaConfig persistenceConfig;

  public JpaInternalModule(
      JpaConfig persistenceConfig) {
    this.persistenceConfig = persistenceConfig;
  }

  @Override
  protected void configure() {
    bind(JpaConfig.class).toInstance(persistenceConfig);
    OptionalBinder.newOptionalBinder(binder(), PersistenceUnitInfo.class)
        .setDefault().toProvider(PersistenceUnitInfoProvider.class);

    bind(JpaService.class).in(Singleton.class);
    bind(PersistService.class).to(JpaService.class);
    bind(UnitOfWork.class).to(JpaService.class);
    bind(EntityManager.class).toProvider(JpaService.class);
    bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryInternalProvider.class);

    bind(JpaManagedService.class).asEagerSingleton();

    bindInterceptor(Matchers.annotatedWith(Transactional.class), Matchers.any(),
        getTransactionInterceptor());
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class),
        getTransactionInterceptor());

    bindInterceptor(Matchers.annotatedWith(javax.transaction.Transactional.class), Matchers.any(),
        getJavaxTransactionInterceptor());
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(javax.transaction.Transactional.class),
        getJavaxTransactionInterceptor());
  }

  protected MethodInterceptor getTransactionInterceptor() {
    MethodInterceptor txInterceptor = new GuiceTransactionInterceptor();
    requestInjection(txInterceptor);
    return txInterceptor;
  }

  protected MethodInterceptor getJavaxTransactionInterceptor() {
    MethodInterceptor txInterceptor = new JavaxTransactionInterceptor();
    requestInjection(txInterceptor);
    return txInterceptor;
  }
}
