package com.github.zhengframework.configuration;

import static com.google.inject.name.Names.named;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class ConfigurationModule extends ConfigurationAwareModule {

  private void bindConfiguration(Binder binder, Configuration configuration) {
    binder = binder.skipSources(Names.class);
    for (String key : configuration.keySet()) {
      binder.bind(Key.get(String.class, named(key)))
          .toProvider(new ConfigurationValueProvider(configuration, key));
    }
  }

  @Override
  protected void configure() {
    Configuration configuration = getConfiguration();
    bind(Configuration.class).toInstance(configuration);
    bindConfiguration(binder(), configuration);
  }
}
