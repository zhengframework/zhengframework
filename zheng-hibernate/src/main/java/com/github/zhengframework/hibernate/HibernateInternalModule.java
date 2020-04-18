package com.github.zhengframework.hibernate;

import com.github.zhengframework.hibernate.HibernatePersistService.EntityManagerFactoryProvider;
import com.github.zhengframework.hibernate.integrator.AutoRegisteringListener;
import com.github.zhengframework.hibernate.integrator.AutoRegisteringListenerIntegrator;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.hibernate.integrator.spi.Integrator;

public class HibernateInternalModule extends AbstractModule {

  private final HibernateConfig hibernateConfig;

  public HibernateInternalModule(
      HibernateConfig hibernateConfig) {
    this.hibernateConfig = hibernateConfig;
  }

  @Override
  protected void configure() {
    String entityPackages = hibernateConfig.getEntityPackages();
    Preconditions
        .checkState(!Strings.isNullOrEmpty(entityPackages), "entityPackages is null or empty");

    bind(HibernateConfig.class).toInstance(hibernateConfig);
    String[] strings = entityPackages.split(",");
    HibernateEntityClassProvider entityClassProvider = new PackageScanEntityClassProvider(
        strings);
    bind(HibernateEntityClassProvider.class).toInstance(entityClassProvider);

    bind(HibernatePersistService.class).in(Singleton.class);
    bind(PersistService.class).to(HibernatePersistService.class);
    bind(UnitOfWork.class).to(HibernatePersistService.class);
    bind(EntityManager.class).toProvider(HibernatePersistService.class);
    bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);
    bind(HibernateManagedService.class).asEagerSingleton();

    ImmutableSet<? extends AutoRegisteringListener> listeners = ImmutableSet.of();
    OptionalBinder.newOptionalBinder(binder(),
        new TypeLiteral<ImmutableSet<? extends AutoRegisteringListener>>() {
        })
        .setDefault().toInstance(listeners);

    Multibinder.newSetBinder(binder(), Integrator.class).addBinding().to(
        AutoRegisteringListenerIntegrator.class
    );

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
    MethodInterceptor txInterceptor = new HibernateTransactionInterceptor();
    requestInjection(txInterceptor);
    return txInterceptor;
  }

  protected MethodInterceptor getJavaxTransactionInterceptor() {
    MethodInterceptor txInterceptor = new HibernateJavaxTransactionInterceptor();
    requestInjection(txInterceptor);
    return txInterceptor;
  }

}
