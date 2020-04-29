package com.github.zhengframework.validator.bval;

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
    OptionalBinder.newOptionalBinder(binder(), MessageInterpolator.class).setBinding()
        .to(DefaultMessageInterpolator.class).in(Scopes.SINGLETON);
    OptionalBinder.newOptionalBinder(binder(), TraversableResolver.class).setBinding()
        .to(DefaultTraversableResolver.class).in(Scopes.SINGLETON);
    OptionalBinder.newOptionalBinder(binder(), ParameterNameProvider.class).setBinding()
        .to(DefaultParameterNameProvider.class);

    bind(ValidatorFactory.class).toProvider(BvalValidatorFactoryProvider.class);
  }
}
