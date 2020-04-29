package com.github.zhengframework.validator.hibernate;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.validator.ValidatorModule;
import com.google.inject.Scopes;
import javax.validation.ValidatorFactory;

public class HibernateValidatorModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    ValidatorModule validatorModule = new ValidatorModule();
    validatorModule.initConfiguration(getConfiguration());
    install(validatorModule);

    bind(ValidatorFactory.class).toProvider(HibernateValidatorFactoryProvider.class)
        .in(Scopes.SINGLETON);
  }
}
