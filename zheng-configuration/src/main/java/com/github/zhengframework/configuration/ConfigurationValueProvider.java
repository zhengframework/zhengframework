package com.github.zhengframework.configuration;

import com.github.zhengframework.core.Configuration;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfigurationValueProvider implements Provider<String> {

  private static final Logger log = LoggerFactory.getLogger(ConfigurationValueProvider.class);
  private final String key;
  private Configuration configuration;

  ConfigurationValueProvider(Configuration configuration, String key) {
    this.key = key;
    this.configuration = configuration;
  }

  @Override
  public String get() {
    log.debug("get value by key=[{}]", key);
    return configuration.get(key).orElse(null);
  }
}
