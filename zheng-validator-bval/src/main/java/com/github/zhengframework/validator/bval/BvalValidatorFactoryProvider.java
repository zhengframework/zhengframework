package com.github.zhengframework.validator.bval;

/*-
 * #%L
 * zheng-validator-bval
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

import com.github.zhengframework.validator.ValidatorConfigurationConfigurer;
import javax.inject.Inject;
import javax.inject.Provider;
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
    ApacheValidatorConfiguration validatorConfiguration =
        Validation.byProvider(ApacheValidationProvider.class).configure();
    validatorConfigurationConfigurer.configure(validatorConfiguration);
    return validatorConfiguration.buildValidatorFactory();
  }
}
