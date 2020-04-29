package com.github.zhengframework.validator;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

@Singleton
public class ExecutableValidatorProvider implements Provider<ExecutableValidator> {

  private final Validator validator;

  public ExecutableValidatorProvider(Validator validator) {
    this.validator = validator;
  }

  @Override
  public ExecutableValidator get() {
    return validator.forExecutables();
  }
}
