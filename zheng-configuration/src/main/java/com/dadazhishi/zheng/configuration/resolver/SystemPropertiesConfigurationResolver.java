package com.dadazhishi.zheng.configuration.resolver;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SystemPropertiesConfigurationResolver extends AbstractConfigurationResolver {

  private final Map<String, String> properties;

  public SystemPropertiesConfigurationResolver() {
    this.properties = Maps.fromProperties(System.getProperties());
  }

  @Override
  protected Map<String, String> delegate() {
    return properties;
  }

  @Override
  public Set<String> keySet() {
    return Collections.emptySet();
  }
}
