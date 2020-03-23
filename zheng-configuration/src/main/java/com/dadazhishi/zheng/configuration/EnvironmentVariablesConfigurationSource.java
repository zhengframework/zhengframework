package com.dadazhishi.zheng.configuration;

import java.util.Collections;

public class EnvironmentVariablesConfigurationSource implements ConfigurationSource {

  @Override
  public Configuration getConfiguration() {
    return new ConfigurationImpl(Collections.unmodifiableMap(System.getenv()));
  }
}
