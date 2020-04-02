package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesConfigurationParser implements AutoConfigurationParser {

  private boolean failOnError = false;

  public void init(Map<String, String> properties) {
    failOnError = Boolean.parseBoolean(properties.getOrDefault("failOnError", "false"));
  }

  @Override
  public Map<String, String> parse(InputStream content) {
    Properties properties = new Properties();
    try {
      properties.load(content);
    } catch (Exception e) {
      if (failOnError) {
        throw new RuntimeException("parse properties fail", e);
      } else {
        log.warn("parse properties fail");
      }
    }
    Map<String, String> map = new HashMap<>();
    for (String name : properties.stringPropertyNames()) {
      map.put(name, properties.getProperty(name));
    }
    return map;
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".properties" };
  }

}
