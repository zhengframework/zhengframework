package com.github.zhengframework.validator.hibernate;

import com.github.zhengframework.validator.ValidatorConfigurationConfigurer;
import com.google.inject.Provider;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;

@Singleton
public class HibernateValidatorFactoryProvider implements Provider<ValidatorFactory> {

  private final ValidatorConfigurationConfigurer validatorConfigurationConfigurer;

  @Inject
  public HibernateValidatorFactoryProvider(
      ValidatorConfigurationConfigurer validatorConfigurationConfigurer) {
    this.validatorConfigurationConfigurer = validatorConfigurationConfigurer;
  }

  public ValidatorFactory get() {
    HibernateValidatorConfiguration validatorConfiguration = Validation
        .byProvider(HibernateValidator.class)
        .configure();
    validatorConfigurationConfigurer.configure(validatorConfiguration);
    return validatorConfiguration.buildValidatorFactory();
  }
}
