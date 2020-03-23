package com.dadazhishi.zheng.configuration;

import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfigurationNamedProvider implements Provider<String> {

  private static final Logger log = LoggerFactory.getLogger(ConfigurationNamedProvider.class);
  private final String key;
  private Configuration configuration;

  public ConfigurationNamedProvider(Configuration configuration, String key) {
    this.key = key;
    this.configuration = configuration;
  }

  @Override
  public String get() {
    log.info("get configuration by key=[{}]", key);
    return configuration.get(key);
  }
}
