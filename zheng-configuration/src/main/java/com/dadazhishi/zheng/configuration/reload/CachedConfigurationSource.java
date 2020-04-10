package com.github.zhengframework.configuration.reload;

import static java.util.Objects.requireNonNull;

import com.github.zhengframework.configuration.environment.Environment;
import com.github.zhengframework.configuration.ex.MissingEnvironmentException;
import com.github.zhengframework.configuration.source.AbstractConfigurationSource;
import com.github.zhengframework.configuration.source.ConfigurationSource;
import java.util.HashMap;
import java.util.Map;

public class CachedConfigurationSource extends AbstractConfigurationSource {

  private final Map<String, Map<String, String>> cachedConfigurationPerEnvironment;
  private final ConfigurationSource underlyingSource;

  public CachedConfigurationSource(ConfigurationSource underlyingSource) {
    this.underlyingSource = requireNonNull(underlyingSource);

    cachedConfigurationPerEnvironment = new HashMap<>();
  }

  @Override
  protected Map<String, String> getConfigurationInternal(Environment environment) {
    if (cachedConfigurationPerEnvironment.containsKey(environment.getName())) {
      return cachedConfigurationPerEnvironment.get(environment.getName());
    } else {
      throw new MissingEnvironmentException(environment.getName());
    }
  }

  @Override
  public void init() {
    underlyingSource.init();
  }

  public void reload(Environment environment) {
    Map<String, String> configuration = underlyingSource.getConfiguration(environment);
    cachedConfigurationPerEnvironment.put(environment.getName(), configuration);
  }
}
