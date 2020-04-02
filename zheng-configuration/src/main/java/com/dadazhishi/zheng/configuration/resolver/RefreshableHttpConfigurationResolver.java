package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefreshableHttpConfigurationResolver extends RefreshableConfigurationResolver {

  private final ConfigurationParser<InputStream> parser;
  private final URL url;
  private final boolean failOnError;

  public RefreshableHttpConfigurationResolver(
      ConfigurationParser<InputStream> parser, URL url) {
    this(parser, url, true);
  }

  public RefreshableHttpConfigurationResolver(
      ConfigurationParser<InputStream> parser, URL url, boolean failOnError) {
    super();
    this.parser = parser;
    this.url = url;
    this.failOnError = failOnError;
  }

  public RefreshableHttpConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser, URL url) {
    this(initialDelay, delay, unit, parser, url, true);
  }

  public RefreshableHttpConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser, URL url, boolean failOnError) {
    super(initialDelay, delay, unit);
    this.parser = parser;
    this.url = url;
    this.failOnError = failOnError;
  }

  @Override
  public void doUpdate() {
    try (InputStream inputStream = url.openStream()) {
      Map<String, String> map = parser.parse(inputStream);
      update(map);
    } catch (IOException e) {
      if (failOnError) {
        throw new RuntimeException("read configuration from url fail, url=" + url, e);
      } else {
        log.warn("read configuration from url fail, url={}", url);
      }

    }
  }
}
