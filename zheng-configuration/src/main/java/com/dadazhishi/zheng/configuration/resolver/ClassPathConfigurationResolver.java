package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ClassPathConfigurationResolver extends AbstractConfigurationResolver {

  private Map<String, String> map = new HashMap<>();

  public ClassPathConfigurationResolver(
      ConfigurationParser<InputStream> parser, String path) {
    this(parser, Thread.currentThread().getContextClassLoader(), path);
  }

  public ClassPathConfigurationResolver(
      ConfigurationParser<InputStream> parser, ClassLoader classLoader, String path) {
    URL resource = classLoader.getResource(path);
    if (resource == null) {
      throw new RuntimeException("invalid class path, path=" + path);
    }
    try (InputStream inputStream = resource.openStream()) {
      map.putAll(parser.parse(inputStream));
    } catch (Exception e) {
      throw new RuntimeException(
          "read configuration from class path fail, path=" + path, e);
    }
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }
}
