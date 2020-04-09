package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.environment.Environment;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MergeConfigurationSource implements ConfigurationSource {

  private final ConfigurationSource[] sources;

  public MergeConfigurationSource(
      ConfigurationSource... sources) {
    this.sources = Objects.requireNonNull(sources);
    for (ConfigurationSource source : sources) {
      Objects.requireNonNull(source);
    }
  }

  @Override
  public void init() {
    for (ConfigurationSource source : sources) {
      source.init();
    }
  }

  @Override
  public void addListener(ConfigurationSourceListener listener) {
    for (ConfigurationSource source : sources) {
      source.addListener(listener);
    }
  }

  @Override
  public void removeListener(ConfigurationSourceListener listener) {
    for (ConfigurationSource source : sources) {
      source.removeListener(listener);
    }
  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    Map<String, String> map = new HashMap<>();
    for (ConfigurationSource source : sources) {
      map.putAll(source.getConfiguration(environment));
    }
    return map;
  }
}
