package com.github.zhengframework.configuration;

import com.google.common.base.Preconditions;
import com.google.inject.PrivateModule;

public abstract class ConfigurationAwarePrivateModule extends PrivateModule implements
    ConfigurationAware {

  private Configuration configuration;

  protected Configuration getConfiguration() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    return configuration;
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

}
