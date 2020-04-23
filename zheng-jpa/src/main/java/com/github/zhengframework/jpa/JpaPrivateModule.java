package com.github.zhengframework.jpa;

import com.github.zhengframework.guice.ExposedPrivateModule;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.persist.PersistFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.spi.PersistenceUnitInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaPrivateModule extends PrivateModule {

  private final Annotation qualifier;

  private final JpaConfig jpaConfig;

  public JpaPrivateModule(Annotation qualifier,
      JpaConfig jpaConfig) {
    this.qualifier = qualifier;
    this.jpaConfig = jpaConfig;
    log.info("qualifier={}", qualifier);
  }

  @Override
  protected void configure() {

    install(new JpaInternalModule(jpaConfig));
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

    Class<? extends ExposedPrivateModule> extraModuleClass = jpaConfig.getExtraModuleClass();
    if (extraModuleClass != null) {
      try {
        ExposedPrivateModule module = extraModuleClass
            .getDeclaredConstructor().newInstance();
        log.info("install extra module: " + extraModuleClass.getName());
        install(module);
        for (Key key : module.getExposeList()) {
          exposeClass(key);
        }
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
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

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void exposeClass(Key key) {
    if (qualifier != null) {
      bind(key.getTypeLiteral()).annotatedWith(qualifier)
          .toProvider(binder().getProvider(key));
      expose(key.getTypeLiteral()).annotatedWith(qualifier);
    } else {
      expose(key.getTypeLiteral());
    }
  }
}
