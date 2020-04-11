package com.github.zhengframework.configuration.source;

import com.github.zhengframework.configuration.environment.Environment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MergeConfigurationSource implements ConfigurationSource {

  private final List<ConfigurationSource> sources = new ArrayList<>();

  public MergeConfigurationSource(ConfigurationSource... sources) {
    Objects.requireNonNull(sources);
    for (ConfigurationSource source : sources) {
      MergeConfigurationSource.this.sources.add(Objects.requireNonNull(source));
    }
  }

  public MergeConfigurationSource(Iterator<ConfigurationSource> sources) {
    Objects.requireNonNull(sources).forEachRemaining(
        configurationSource -> MergeConfigurationSource.this.sources
            .add(Objects.requireNonNull(configurationSource)));
  }

  public MergeConfigurationSource(Iterable<ConfigurationSource> sources) {
    Objects.requireNonNull(sources).forEach(
        configurationSource -> MergeConfigurationSource.this.sources
            .add(Objects.requireNonNull(configurationSource)));
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
      Map<String, String> configuration = source.getConfiguration(environment);
      map.putAll(configuration);
    }
    return map;
  }
}
