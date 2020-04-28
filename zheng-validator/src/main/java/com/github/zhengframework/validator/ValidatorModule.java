package com.github.zhengframework.validator;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.OptionalBinder;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;

public class ValidatorModule extends AbstractModule {

  @Override
  protected void configure() {
    OptionalBinder.newOptionalBinder(binder(),
        ConstraintValidatorFactory.class)
        .setDefault().to(GuiceConstraintValidatorFactory.class);
    OptionalBinder.newOptionalBinder(binder(), MessageInterpolator.class);
    OptionalBinder.newOptionalBinder(binder(), TraversableResolver.class);
    OptionalBinder.newOptionalBinder(binder(), ParameterNameProvider.class);
    OptionalBinder.newOptionalBinder(binder(), ValidatorConfigurationConfigurer.class)
        .setDefault().to(ValidatorConfigurationConfigurerImpl.class);

    bind(Validator.class).toProvider(ValidatorProvider.class);

    Provider<Validator> validatorProvider = getProvider(Validator.class);
    ValidationMethodInterceptor methodInterceptor = new ValidationMethodInterceptor(
        validatorProvider);

    bindInterceptor(Matchers.any(),
        Matchers.annotatedWith(Validate.class), methodInterceptor);
  }
}
