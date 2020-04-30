package com.github.zhengframework.validator;

import com.google.inject.Provider;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Singleton
public class ValidatorProvider implements Provider<Validator> {

  private final ValidatorFactory validatorFactory;

  @Inject
  public ValidatorProvider(ValidatorFactory validatorFactory) {
    this.validatorFactory = validatorFactory;
  }

  public Validator get() {
    return validatorFactory.getValidator();
  }
}
