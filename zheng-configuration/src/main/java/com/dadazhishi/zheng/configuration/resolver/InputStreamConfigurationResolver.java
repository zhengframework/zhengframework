package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InputStreamConfigurationResolver extends ReloadableConfigurationResolver {

  private final ConfigurationParser<InputStream> parser;
  private final Supplier<InputStream> inputStreamSupplier;
  private final boolean failOnError;

  public InputStreamConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier) {
    this(parser, inputStreamSupplier, true);
  }

  public InputStreamConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier, boolean failOnError) {
    this.parser = parser;
    this.inputStreamSupplier = inputStreamSupplier;
    this.failOnError = failOnError;
    reload();
  }

  @Override
  public void reload() {
    try (InputStream inputStream = inputStreamSupplier.get()) {
      if (inputStream != null) {
        update(parser.parse(inputStream));
      }
    } catch (IOException e) {
      if (failOnError) {
        throw new RuntimeException("get configuration from InputStream fail", e);
      } else {
        log.warn("get configuration from InputStream fail");
      }
    }
  }
}
