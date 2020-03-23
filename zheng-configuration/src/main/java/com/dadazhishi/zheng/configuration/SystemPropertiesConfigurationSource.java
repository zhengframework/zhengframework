package com.dadazhishi.zheng.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SystemPropertiesConfigurationSource implements ConfigurationSource {

  @Override
  public Configuration getConfiguration() {
    Properties properties = System.getProperties();
    Map<String, String> map = new HashMap<>();
    for (String name : properties.stringPropertyNames()) {
      map.put(name, properties.getProperty(name));
    }
    return new ConfigurationImpl(Collections.unmodifiableMap(map));
  }
}
