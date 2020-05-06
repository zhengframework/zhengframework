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
