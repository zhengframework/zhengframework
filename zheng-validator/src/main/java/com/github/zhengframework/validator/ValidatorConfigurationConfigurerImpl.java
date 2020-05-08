package com.github.zhengframework.validator;

/*-
 * #%L
 * zheng-validator
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
