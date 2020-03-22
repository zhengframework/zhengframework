package com.dadazhishi.zheng.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.OptionalBinder;

public class ConfigurationModule extends AbstractModule {

  @Override
  protected void configure() {
    OptionalBinder.newOptionalBinder(binder(), Configuration.class)
        .setDefault().to(ConfigurationImpl.class);
  }
}
