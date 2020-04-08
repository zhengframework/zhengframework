package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.environment.Environment;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SystemPropertiesConfigurationSource implements ConfigurationSource {

  @Override
  public void init() {

  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    return Collections.unmodifiableMap(System.getProperties()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> Objects.toString(entry.getKey()),
            entry -> Objects.toString(entry.getValue())
        )));
  }
}
