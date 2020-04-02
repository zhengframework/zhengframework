package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassPathConfigurationResolver extends AbstractConfigurationResolver {

  private Map<String, String> map = new HashMap<>();

  public ClassPathConfigurationResolver(
      ConfigurationParser<InputStream> parser, String path) {
    this(parser, Thread.currentThread().getContextClassLoader(), path, true);
  }

  public ClassPathConfigurationResolver(
      ConfigurationParser<InputStream> parser, ClassLoader classLoader, String path,
      boolean failOnError) {
    Preconditions.checkState(!Strings.isNullOrEmpty(path), "path is null or empty");
    URL resource = classLoader.getResource(path);
    if (resource == null) {
      throw new RuntimeException("invalid class path, path=" + path);
    }
    try (InputStream inputStream = resource.openStream()) {
      map.putAll(parser.parse(inputStream));
    } catch (Exception e) {
      if (failOnError) {
        throw new RuntimeException(
            "read configuration from class path fail, path=" + path, e);
      } else {
        log.warn("read configuration from class path fail, path={}", path);
      }
    }
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }
}
