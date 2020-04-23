package com.github.zhengframework.mybatis;

import com.github.zhengframework.guice.ExposedPrivateModule;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBatisXmlPrivateModule extends PrivateModule {

  private final Annotation qualifier;
  private final MyBatisConfig myBatisConfig;

  public MyBatisXmlPrivateModule(Annotation qualifier,
      MyBatisConfig myBatisConfig) {
    this.qualifier = qualifier;
    this.myBatisConfig = myBatisConfig;
    log.info("qualifier={}", qualifier);
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected void configure() {
    install(new MyBatisXmlInternalModule(myBatisConfig));
    Class<? extends ExposedPrivateModule> extraModuleClass = myBatisConfig.getExtraModuleClass();
    if (extraModuleClass != null) {
      try {
        ExposedPrivateModule module = extraModuleClass.getDeclaredConstructor().newInstance();
        log.info("install module: " + extraModuleClass.getName());
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
  private void exposeClass(Key key) {
    if (qualifier != null) {
      bind(key.getTypeLiteral()).annotatedWith(qualifier)
          .toProvider(binder().getProvider(key));
      expose(key.getTypeLiteral()).annotatedWith(qualifier);
    } else {
      expose(key.getTypeLiteral());
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
