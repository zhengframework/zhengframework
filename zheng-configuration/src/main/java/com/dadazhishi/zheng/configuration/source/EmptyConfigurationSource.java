package com.github.zhengframework.configuration.source;

import com.github.zhengframework.configuration.environment.Environment;
import java.util.Collections;
import java.util.Map;

public class EmptyConfigurationSource implements ConfigurationSource {

  private final Map<String, String> map = Collections.emptyMap();

  @Override
  public void init() {

  }

  @Override
  public void addListener(ConfigurationSourceListener listener) {

  }

  @Override
  public void removeListener(ConfigurationSourceListener listener) {

  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    return map;
  }
}
