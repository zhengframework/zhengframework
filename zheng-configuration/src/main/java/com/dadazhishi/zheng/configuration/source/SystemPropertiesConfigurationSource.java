package com.github.zhengframework.configuration.source;

import com.github.zhengframework.configuration.environment.Environment;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SystemPropertiesConfigurationSource extends AbstractConfigurationSource {

  @Override
  public void init() {

  }

  @Override
  public Map<String, String> getConfigurationInternal(Environment environment) {
    return Collections.unmodifiableMap(System.getProperties()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> Objects.toString(entry.getKey()),
            entry -> Objects.toString(entry.getValue())
        )));
  }
}
