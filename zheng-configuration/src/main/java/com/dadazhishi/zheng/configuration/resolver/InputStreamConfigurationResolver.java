package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InputStreamConfigurationResolver extends AbstractConfigurationResolver {

  private Map<String, String> map = new HashMap<>();

  public InputStreamConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier) {
    this(parser, inputStreamSupplier, true);
  }

  public InputStreamConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier, boolean failOnError) {
    try (InputStream inputStream = inputStreamSupplier.get()) {
      map.putAll(parser.parse(inputStream));
    } catch (IOException e) {
      if (failOnError) {
        throw new RuntimeException("get configuration from InputStream fail", e);
      } else {
        log.warn("get configuration from InputStream fail");
      }
    }
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }
}
