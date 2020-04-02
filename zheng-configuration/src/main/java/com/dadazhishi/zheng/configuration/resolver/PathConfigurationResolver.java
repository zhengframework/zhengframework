package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathConfigurationResolver extends AbstractConfigurationResolver {

  private Map<String, String> map = new HashMap<>();

  public PathConfigurationResolver(
      ConfigurationParser<InputStream> parser, Path path) {
    this(parser, path, true);
  }

  public PathConfigurationResolver(
      ConfigurationParser<InputStream> parser, Path path, boolean failOnError) {
    try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ)) {
      map.putAll(parser.parse(inputStream));
    } catch (Exception e) {
      if (failOnError) {
        throw new RuntimeException(
            "read configuration from Path fail, path=" + path, e);
      } else {
        log.warn("read configuration from Path fail, path={}", path);
      }
    }
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }
}
