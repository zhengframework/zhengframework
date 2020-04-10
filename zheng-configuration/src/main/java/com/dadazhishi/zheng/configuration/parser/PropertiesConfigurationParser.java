package com.github.zhengframework.configuration.parser;

import com.github.zhengframework.configuration.ex.ConfigurationSourceException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfigurationParser implements ConfigurationParser, FileConfigurationParser {

  @Override
  public Map<String, String> parse(InputStream inputStream) {
    Properties properties = new Properties();
    try {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from inputStream", e);
    }
    Map<String, String> map = new HashMap<>();
    for (String name : properties.stringPropertyNames()) {
      map.put(name, properties.getProperty(name));
    }
    return map;
  }

  @Override
  public String[] supportFileTypes() {
    return new String[]{".properties"};
  }

  @Override
  public Map<String, String> parse(String fileName, InputStream inputStream) {
    checkSupportFileTypes(fileName);
    Properties properties = new Properties();
    try {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from file: " + fileName, e);
    }
    Map<String, String> map = new HashMap<>();
    for (String name : properties.stringPropertyNames()) {
      map.put(name, properties.getProperty(name));
    }
    return map;
  }
}
