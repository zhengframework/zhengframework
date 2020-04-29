package com.github.zhengframework.validator;

import com.google.inject.Inject;
import java.util.Optional;
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
    Optional.ofNullable(messageInterpolator).ifPresent(configuration::messageInterpolator);
    Optional.ofNullable(traversableResolver).ifPresent(configuration::traversableResolver);
    Optional.ofNullable(parameterNameProvider).ifPresent(configuration::parameterNameProvider);
    Optional.ofNullable(constraintValidatorFactory)
        .ifPresent(configuration::constraintValidatorFactory);
  }
}
