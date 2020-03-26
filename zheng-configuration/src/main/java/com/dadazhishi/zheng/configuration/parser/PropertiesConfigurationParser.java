package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfigurationParser implements AutoConfigurationParser {

  @Override
  public Map<String, String> parse(InputStream content) {
    Properties properties = new Properties();
    try {
      properties.load(content);
    } catch (Exception e) {
      throw new RuntimeException("parse properties fail", e);
    }
    Map<String, String> map = new HashMap<>();
    for (String name : properties.stringPropertyNames()) {
      map.put(name, properties.getProperty(name));
    }
    return map;
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".properties"};
  }

}
