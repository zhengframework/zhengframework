package com.github.zhengframework.jpa;

import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.persist.PersistFilter;
import java.lang.annotation.Annotation;
import java.util.Objects;
import javax.persistence.spi.PersistenceUnitInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaPrivateModule extends PrivateModule {

  private final Annotation qualifier;

  private final JpaConfig persistenceConfig;

  public JpaPrivateModule(Annotation qualifier,
      JpaConfig persistenceConfig) {
    this.qualifier = qualifier;
    this.persistenceConfig = persistenceConfig;
    log.info("qualifier={}", qualifier);
  }

  @Override
  protected void configure() {

    install(new JpaInternalModule(persistenceConfig));
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
    } catch (ClassNotFoundException ignored) {

    }
    Class<?>[] classes = new Class<?>[]{
        JpaManagedService.class, PersistenceUnitInfo.class};
    for (Class<?> aClass : classes) {
      exposeClass(aClass);
    }

    for (Class<?> aClass : Objects.requireNonNull(persistenceConfig.getExposeClassSet())) {
      log.info("bind and expose class={}", aClass.getName());
      bind(aClass);
      exposeClass(aClass);
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
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
