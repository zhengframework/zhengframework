package com.dadazhishi.zheng.configuration;

import java.util.Collections;
import java.util.Map;

public class MapConfigurationSource implements ConfigurationSource {

  private final Map<String, String> map;

  public MapConfigurationSource(Map<String, String> map) {
    this.map = map;
  }

  @Override
  public Configuration getConfiguration() {
    return new ConfigurationImpl(Collections.unmodifiableMap(map));
  }
}
