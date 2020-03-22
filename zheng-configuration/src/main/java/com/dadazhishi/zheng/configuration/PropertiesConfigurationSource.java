package com.dadazhishi.zheng.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfigurationSource implements ConfigurationSource {

  private final Properties properties;

  public PropertiesConfigurationSource(Properties properties) {

    this.properties = properties;
  }

  public static PropertiesConfigurationSource loadFromFile(String propertiesFile) throws IOException {
    Properties properties = new Properties();
    properties.load(new FileReader(propertiesFile));
    return new PropertiesConfigurationSource(properties);
  }

  public static PropertiesConfigurationSource load(File propertiesFile) throws IOException {
    Properties properties = new Properties();
    properties.load(new FileReader(propertiesFile));
    return new PropertiesConfigurationSource(properties);
  }

  public static PropertiesConfigurationSource load(Reader reader) throws IOException {
    Properties properties = new Properties();
    properties.load(reader);
    return new PropertiesConfigurationSource(properties);
  }

  public static PropertiesConfigurationSource load(InputStream inputStream) throws IOException {
    Properties properties = new Properties();
    properties.load(inputStream);
    return new PropertiesConfigurationSource(properties);
  }

  @Override
  public Configuration read() {
    Map<String, String> map = new HashMap<>();
    for (String name : properties.stringPropertyNames()) {
      map.put(name, properties.getProperty(name));
    }
    return new ConfigurationImpl("", Collections.unmodifiableMap(map));
  }
}
