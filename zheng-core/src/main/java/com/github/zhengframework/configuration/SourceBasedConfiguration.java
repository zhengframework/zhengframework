package com.github.zhengframework.configuration;

import com.github.zhengframework.configuration.environment.Environment;
import com.github.zhengframework.configuration.source.ConfigurationSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public class SourceBasedConfiguration extends AbstractConfiguration {

  private final ConfigurationSource configurationSource;
  private final Environment environment;

  public SourceBasedConfiguration(
      ConfigurationSource configurationSource,
      Environment environment) {
    this.configurationSource = configurationSource;
    this.environment = environment;
  }

  @Override
  public Optional<String> get(String key) {
    Map<String, String> configuration = configurationSource.getConfiguration(environment);
    String value = configuration.get(key);
    if (value != null) {
      PlaceHolder placeHolder = new PlaceHolder(this);
      return Optional.ofNullable(placeHolder.replace(value));
    }
    return Optional.empty();
  }

  @Override
  public Set<String> keySet() {
    return configurationSource.getConfiguration(environment).keySet();
  }

  @Override
  public Map<String, String> asMap() {
    Map<String, String> configuration = configurationSource.getConfiguration(environment);
    Map<String, String> copy = new HashMap<>();
    PlaceHolder placeHolder = new PlaceHolder(this);
    for (Entry<String, String> entry : configuration.entrySet()) {
      copy.put(entry.getKey(), placeHolder.replace(entry.getValue()));
    }
    return copy;
  }

  @Override
  public String toString() {
    return asMap().toString();
  }

}
