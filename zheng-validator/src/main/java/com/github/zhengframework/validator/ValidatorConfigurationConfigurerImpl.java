package com.github.zhengframework.validator;

import com.google.inject.Inject;
import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;

public class ValidatorConfigurationConfigurerImpl implements ValidatorConfigurationConfigurer {

  @Inject(optional = true)
  private MessageInterpolator messageInterpolator;
  @Inject(optional = true)
  private TraversableResolver traversableResolver;
  @Inject(optional = true)
  private ParameterNameProvider parameterNameProvider;
  @Inject(optional = true)
  private ConstraintValidatorFactory constraintValidatorFactory;

  @Override
  public void configure(Configuration<?> configuration) {
    if (messageInterpolator != null) {
      configuration.messageInterpolator(messageInterpolator);
    }
    if (traversableResolver != null) {
      configuration.traversableResolver(traversableResolver);
    }
    if (parameterNameProvider != null) {
      configuration.parameterNameProvider(parameterNameProvider);
    }
    if (constraintValidatorFactory != null) {
      configuration.constraintValidatorFactory(constraintValidatorFactory);
    }

  }
}
