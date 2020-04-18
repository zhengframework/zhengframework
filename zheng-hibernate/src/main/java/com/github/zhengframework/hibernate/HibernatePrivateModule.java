package com.github.zhengframework.hibernate;

import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.persist.PersistFilter;
import java.lang.annotation.Annotation;
import java.util.Objects;

@SuppressWarnings({"rawtypes", "unchecked"})
public class HibernatePrivateModule extends PrivateModule {

  private final Annotation qualifier;

  private final HibernateConfig hibernateConfig;

  public HibernatePrivateModule(Annotation qualifier,
      HibernateConfig hibernateConfig) {
    this.qualifier = qualifier;
    this.hibernateConfig = hibernateConfig;
  }

  @Override
  protected void configure() {
    install(new HibernateInternalModule(hibernateConfig));

    try {
      Class.forName("javax.servlet.Filter");
      if (qualifier == null) {
        Key<PersistFilter> key = Key.get(PersistFilter.class);
        bind(key).to(PersistFilter.class);
        expose(key);
      } else {
        Key<PersistFilter> key = Key.get(PersistFilter.class, qualifier);
        bind(key).to(PersistFilter.class);
        expose(key);
      }
    } catch (ClassNotFoundException e) {

    }

    Class<?>[] classes = new Class<?>[]{
        HibernateManagedService.class};
    for (Class<?> aClass : classes) {
      exposeClass(aClass);
    }

    for (Class<?> aClass : Objects.requireNonNull(hibernateConfig.getClassCollection())) {
      bind(aClass);
      exposeClass(aClass);
    }
  }

  private void exposeClass(Class aClass) {
    if (qualifier != null) {
      bind(aClass).annotatedWith(qualifier)
          .toProvider(binder().getProvider(aClass));
      expose(aClass).annotatedWith(qualifier);
    } else {
      expose(aClass);
    }
  }

}
