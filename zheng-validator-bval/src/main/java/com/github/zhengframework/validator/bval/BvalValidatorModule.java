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

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.validator.ValidatorModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.OptionalBinder;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidatorFactory;
import org.apache.bval.jsr.DefaultMessageInterpolator;
import org.apache.bval.jsr.parameter.DefaultParameterNameProvider;
import org.apache.bval.jsr.resolver.DefaultTraversableResolver;

public class BvalValidatorModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    ValidatorModule validatorModule = new ValidatorModule();
    validatorModule.initConfiguration(getConfiguration());
    install(validatorModule);
    OptionalBinder.newOptionalBinder(binder(), MessageInterpolator.class)
        .setBinding()
        .to(DefaultMessageInterpolator.class)
        .in(Scopes.SINGLETON);
    OptionalBinder.newOptionalBinder(binder(), TraversableResolver.class)
        .setBinding()
        .to(DefaultTraversableResolver.class)
        .in(Scopes.SINGLETON);
    OptionalBinder.newOptionalBinder(binder(), ParameterNameProvider.class)
        .setBinding()
        .to(DefaultParameterNameProvider.class);

    bind(ValidatorFactory.class).toProvider(BvalValidatorFactoryProvider.class);
  }
}
