package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpConfigurationResolver extends AbstractConfigurationResolver {

  private Map<String, String> map = new HashMap<>();

  public HttpConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      URL url) {
    this(parser, url, true);
  }

  public HttpConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      URL url, boolean failOnError) {
    try (InputStream inputStream = url.openStream()) {
      map.putAll(parser.parse(inputStream));
    } catch (IOException e) {
      if (failOnError) {
        throw new RuntimeException("get configuration from URL fail, url=" + url, e);
      } else {
        log.warn("get configuration from URL fail, url={}", url);
      }

    }
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }

}
