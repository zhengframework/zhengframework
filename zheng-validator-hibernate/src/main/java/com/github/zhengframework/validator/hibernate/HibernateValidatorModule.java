package com.github.zhengframework.validator.hibernate;

import com.github.zhengframework.validator.ValidatorModule;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import javax.validation.ValidatorFactory;

public class HibernateValidatorModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new ValidatorModule());

    bind(ValidatorFactory.class).toProvider(HibernateValidatorFactoryProvider.class)
        .in(Scopes.SINGLETON);
  }
}
