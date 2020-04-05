package com.dadazhishi.zheng.configuration;

import static com.google.inject.name.Names.named;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class ConfigurationModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  private void bindConfiguration(Binder binder, Configuration configuration) {
    binder = binder.skipSources(Names.class);
    for (String key : configuration.keySet()) {
      binder.bind(Key.get(String.class, named(key)))
          .toProvider(new ConfigurationValueProvider(configuration, key));
    }
  }

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    bind(Configuration.class).toInstance(configuration);
    bindConfiguration(binder(), configuration);
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
