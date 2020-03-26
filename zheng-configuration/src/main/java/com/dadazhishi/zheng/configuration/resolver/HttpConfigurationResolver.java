package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpConfigurationResolver extends AbstractConfigurationResolver {

  private Map<String, String> map = new HashMap<>();

  public HttpConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      URL url) {
    try (InputStream inputStream = url.openStream()) {
      map.putAll(parser.parse(inputStream));
    } catch (IOException e) {
      throw new RuntimeException("get configuration from url fail, url=" + url, e);
    }
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }

}
