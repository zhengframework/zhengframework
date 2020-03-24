package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class InputStreamConfigurationResolver extends AbstractConfigurationResolver {

  private Map<String, String> map = new HashMap<>();

  public InputStreamConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier) {
    try (InputStream inputStream = inputStreamSupplier.get()) {
      map.putAll(parser.parse(inputStream));
    } catch (IOException e) {
      throw new RuntimeException("get configuration from InputStream fail", e);
    }
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }
}
