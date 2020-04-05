package com.dadazhishi.zheng.configuration.resolver;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class SystemConfigurationResolver extends ReloadableConfigurationResolver {

  @Override
  public void reload() {
    update(Collections.unmodifiableMap(System.getProperties()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> Objects.toString(entry.getKey()),
            entry -> Objects.toString(entry.getValue())
        ))));
  }

}
