package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefreshableFileConfigurationResolver extends RefreshableConfigurationResolver {

  private final ConfigurationParser<InputStream> parser;

  private final File file;

  private final boolean failOnError;

  public RefreshableFileConfigurationResolver(
      ConfigurationParser<InputStream> parser, File file) {
    this(parser, file, true);
  }

  public RefreshableFileConfigurationResolver(
      ConfigurationParser<InputStream> parser, File file, boolean failOnError) {
    super();
    this.parser = parser;
    this.file = file;
    this.failOnError = failOnError;
  }

  public RefreshableFileConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser, File file) {
    this(initialDelay, delay, unit, parser, file, true);
  }

  public RefreshableFileConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser, File file, boolean failOnError) {
    super(initialDelay, delay, unit);
    this.parser = parser;
    this.file = file;
    this.failOnError = failOnError;
  }

  @Override
  public void doUpdate() {
    try (InputStream inputStream = new FileInputStream(file)) {
      Map<String, String> map = parser.parse(inputStream);
      update(map);
    } catch (Exception e) {
      if (failOnError) {
        throw new RuntimeException(
            "read configuration from file fail, file=" + file, e);
      } else {
        log.warn("read configuration from file fail, file={}", file);
      }

    }
  }
}
