package com.dadazhishi.zheng.configuration.resolver;

import java.util.Map;

public class MapConfigurationResolver extends AbstractConfigurationResolver {

  private final Map<String, String> properties;

  public MapConfigurationResolver(Map<String, String> properties) {
    this.properties = properties;
  }

  @Override
  protected Map<String, String> delegate() {
    return properties;
  }

}
