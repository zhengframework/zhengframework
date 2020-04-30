package com.github.zhengframework.validator;

import com.google.inject.Injector;
import com.google.inject.Provider;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

public class GuiceConstraintValidatorFactory implements ConstraintValidatorFactory {

  private final Provider<Injector> injectorProvider;

  @Inject
  public GuiceConstraintValidatorFactory(
      Provider<Injector> injectorProvider) {
    this.injectorProvider = injectorProvider;
  }

  @Override
  public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> aClass) {
    return injectorProvider.get().getInstance(aClass);
  }

  @Override
  public void releaseInstance(ConstraintValidator<?, ?> constraintValidator) {

  }
}
