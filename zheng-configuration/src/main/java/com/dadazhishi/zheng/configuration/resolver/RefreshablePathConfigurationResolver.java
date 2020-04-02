package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefreshablePathConfigurationResolver extends RefreshableConfigurationResolver {

  private final ConfigurationParser<InputStream> parser;

  private final Path path;

  private final boolean failOnError;

  public RefreshablePathConfigurationResolver(
      ConfigurationParser<InputStream> parser, Path path) {
    this(parser, path, true);
  }

  public RefreshablePathConfigurationResolver(
      ConfigurationParser<InputStream> parser, Path path, boolean failOnError) {
    super();
    this.parser = parser;
    this.path = path;
    this.failOnError = failOnError;
  }

  public RefreshablePathConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser, Path path) {
    this(initialDelay, delay, unit, parser, path, true);
  }

  public RefreshablePathConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser, Path path, boolean failOnError) {
    super(initialDelay, delay, unit);
    this.parser = parser;
    this.path = path;
    this.failOnError = failOnError;
  }

  @Override
  public void doUpdate() {
    try (InputStream inputStream = Files.newInputStream(path)) {
      Map<String, String> map = parser.parse(inputStream);
      update(map);
    } catch (Exception e) {
      if (failOnError) {
        throw new RuntimeException(
            "read configuration from Path fail, path=" + path, e);
      } else {
        log.warn("read configuration from Path fail, path={}", path);
      }

    }
  }
}
