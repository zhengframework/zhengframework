package com.github.zhengframework.hibernate;

import com.github.zhengframework.hibernate.HibernatePersistService.EntityManagerFactoryProvider;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.PrivateModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;
import java.lang.annotation.Annotation;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.aopalliance.intercept.MethodInterceptor;

@SuppressWarnings({"rawtypes", "unchecked"})
public class HibernatePersistPrivateModule extends PrivateModule {

  private final Annotation qualifier;

  private final HibernateConfig hibernateConfig;

  public HibernatePersistPrivateModule(Annotation qualifier,
      HibernateConfig hibernateConfig) {
    this.qualifier = qualifier;
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
    bind(HibernateService.class).asEagerSingleton();
    MethodInterceptor txInterceptor = new HibernateTransactionInterceptor();
    requestInjection(txInterceptor);
    this.bindInterceptor(Matchers.annotatedWith(Transactional.class), Matchers.any(),
        txInterceptor);
    this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class),
        txInterceptor);

//    for (Class<?> aClass : entityClassProvider.get()) {
//      if (qualifier != null) {
//        bind(aClass).annotatedWith(qualifier);
//        expose(aClass).annotatedWith(qualifier);
//      } else {
//        bind(aClass);
//        expose(aClass);
//      }
//    }

//    if(qualifier==null){
//      Key<PersistFilter> key = Key.get(PersistFilter.class);
//      bind(key).to(PersistFilter.class);
//      expose(key);
//    }else {
//      Key<PersistFilter> key = Key.get(PersistFilter.class, qualifier);
//      bind(key).to(PersistFilter.class);
//      expose(key);
//    }

    Class[] classes = new Class[]{EntityManagerFactory.class,
        EntityManager.class,
//        PersistService.class,
//        UnitOfWork.class,
        HibernatePersistService.class, HibernateService.class};
    for (Class aClass : classes) {
      if (qualifier != null) {
        bind(aClass).annotatedWith(qualifier)
            .toProvider(binder().getProvider(aClass));
        expose(aClass).annotatedWith(qualifier);
      } else {
        expose(aClass);
      }
    }

  }

}
