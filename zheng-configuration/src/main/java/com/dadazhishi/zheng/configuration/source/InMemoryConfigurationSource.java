package com.github.zhengframework.configuration.source;

import static com.github.zhengframework.configuration.source.EnvironmentVariablesConfigurationSource.convertToPropertiesKey;
import static com.github.zhengframework.configuration.source.EnvironmentVariablesConfigurationSource.formatEnvironmentContext;

import com.github.zhengframework.configuration.environment.Environment;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class InMemoryConfigurationSource extends AbstractConfigurationSource {

  private final Map<String, String> map;

  public InMemoryConfigurationSource(Map<String, String> map) {
    this.map = Collections.unmodifiableMap(Objects.requireNonNull(map));
  }

  @Override
  public void init() {

  }

  @Override
  protected Map<String, String> getConfigurationInternal(Environment environment) {
    String environmentContext = formatEnvironmentContext(environment);
    Map<String, String> copyMap = new HashMap<>();
    for (Entry<String, String> entry : map.entrySet()) {
      if (entry.getKey().startsWith(environmentContext)) {
        copyMap
            .put(convertToPropertiesKey(entry.getKey(), environmentContext), entry.getValue());
      }
    }
    return Collections.unmodifiableMap(copyMap);
  }

}
