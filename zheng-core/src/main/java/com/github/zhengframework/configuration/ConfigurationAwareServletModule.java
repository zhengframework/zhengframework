package com.github.zhengframework.configuration;

import com.google.inject.servlet.ServletModule;

public class ConfigurationAwareServletModule extends ServletModule implements ConfigurationAware {

  private Configuration configuration;

  protected Configuration getConfiguration() {
    return configuration;
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
