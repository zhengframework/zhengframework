package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RefreshableHttpConfigurationResolver extends RefreshableConfigurationResolver {

  private final ConfigurationParser<InputStream> parser;
  private final URL url;

  public RefreshableHttpConfigurationResolver(
      ConfigurationParser<InputStream> parser, URL url) {
    super();
    this.parser = parser;
    this.url = url;
  }

  public RefreshableHttpConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser, URL url) {
    super(initialDelay, delay, unit);
    this.parser = parser;
    this.url = url;
  }

  @Override
  public void doUpdate() {
    try (InputStream inputStream = url.openStream()) {
      Map<String, String> map = parser.parse(inputStream);
      update(map);
    } catch (IOException e) {
      throw new RuntimeException("get configuration from url fail, url=" + url, e);
    }
  }
}
