package com.github.zhengframework.validator.bval;

import com.github.zhengframework.validator.ValidatorConfigurationConfigurer;
import com.google.inject.Provider;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.apache.bval.jsr.ApacheValidationProvider;
import org.apache.bval.jsr.ApacheValidatorConfiguration;

@Singleton
public class BvalValidatorFactoryProvider implements Provider<ValidatorFactory> {

  private final ValidatorConfigurationConfigurer validatorConfigurationConfigurer;

  @Inject
  public BvalValidatorFactoryProvider(
      ValidatorConfigurationConfigurer validatorConfigurationConfigurer) {
    this.validatorConfigurationConfigurer = validatorConfigurationConfigurer;
  }

  public ValidatorFactory get() {
    ApacheValidatorConfiguration validatorConfiguration = Validation
        .byProvider(ApacheValidationProvider.class)
        .configure();
    validatorConfigurationConfigurer.configure(validatorConfiguration);
    return validatorConfiguration.buildValidatorFactory();
  }
}
