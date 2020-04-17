package com.github.zhengframework.configuration;


import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MapConfiguration extends AbstractConfiguration {

  private final Map<String, String> map;

  public MapConfiguration(Map<String, String> map) {
    this.map = Collections.unmodifiableMap(map);
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(map.get(key));
  }

  @Override
  public Set<String> keySet() {
    return map.keySet();
  }

  @Override
  public Map<String, String> asMap() {
    return map;
  }

  @Override
  public String toString() {
    return asMap().toString();
  }
}
