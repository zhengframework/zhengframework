package com.dadazhishi.zheng.configuration.resolver;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesConfigurationResolver extends AbstractConfigurationResolver {

  private static final Logger logger = LoggerFactory
      .getLogger(PropertiesConfigurationResolver.class);
  private volatile Map<String, String> properties = Maps.newHashMap();

  public PropertiesConfigurationResolver(String path) {
    try {
      this.properties = loadProperties(path);
    } catch (IOException e) {
      logger.warn("Error loading properties from file: " + path);
    }
  }

  private static Map<String, String> loadProperties(String file) throws IOException {
    Path path = Paths.get(file);
    if (Files.exists(path)) {
      try (Reader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
        Properties properties = new Properties();
        properties.load(r);
        return Maps.fromProperties(properties);
      }
    } else {
      return Collections.emptyMap();
    }
  }

  @Override
  protected Map<String, String> delegate() {
    return properties;
  }
}
