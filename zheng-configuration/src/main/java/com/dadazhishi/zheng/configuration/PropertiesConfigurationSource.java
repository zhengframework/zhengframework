package com.dadazhishi.zheng.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfigurationSource implements ConfigurationSource {

  private final Properties properties;

  public PropertiesConfigurationSource(Properties properties) {
    this.properties = properties;
  }

  public PropertiesConfigurationSource(String propertiesFile) throws IOException {
    properties = new Properties();
    properties.load(new FileReader(propertiesFile));
  }

  public PropertiesConfigurationSource(File propertiesFile) throws IOException {
    properties = new Properties();
    properties.load(new FileReader(propertiesFile));
  }

  public PropertiesConfigurationSource(Reader reader) throws IOException {
    properties = new Properties();
    properties.load(reader);
  }

  public PropertiesConfigurationSource(InputStream inputStream) throws IOException {
    properties = new Properties();
    properties.load(inputStream);
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
