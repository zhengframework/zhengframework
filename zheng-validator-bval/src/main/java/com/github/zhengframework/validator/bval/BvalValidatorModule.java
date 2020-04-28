package com.github.zhengframework.validator.bval;

import com.github.zhengframework.validator.ValidatorModule;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.OptionalBinder;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidatorFactory;
import org.apache.bval.jsr.DefaultMessageInterpolator;
import org.apache.bval.jsr.parameter.DefaultParameterNameProvider;
import org.apache.bval.jsr.resolver.DefaultTraversableResolver;

public class BvalValidatorModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new ValidatorModule());
    OptionalBinder.newOptionalBinder(binder(), MessageInterpolator.class).setBinding()
        .to(DefaultMessageInterpolator.class).in(Scopes.SINGLETON);
    OptionalBinder.newOptionalBinder(binder(), TraversableResolver.class).setBinding()
        .to(DefaultTraversableResolver.class).in(Scopes.SINGLETON);
    OptionalBinder.newOptionalBinder(binder(), ParameterNameProvider.class).setBinding()
        .to(DefaultParameterNameProvider.class);

    bind(ValidatorFactory.class).toProvider(BvalValidatorFactoryProvider.class);
  }
}
