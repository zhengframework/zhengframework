package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RefreshableInputStreamConfigurationResolver extends RefreshableConfigurationResolver {

  private final ConfigurationParser<InputStream> parser;

  private final Supplier<InputStream> inputStreamSupplier;

  public RefreshableInputStreamConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier) {
    super();
    this.parser = parser;
    this.inputStreamSupplier = inputStreamSupplier;
  }

  public RefreshableInputStreamConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier) {
    super(initialDelay, delay, unit);
    this.parser = parser;
    this.inputStreamSupplier = inputStreamSupplier;
  }

  @Override
  public void doUpdate() {
    try (InputStream inputStream = inputStreamSupplier.get()) {
      Map<String, String> map = parser.parse(inputStream);
      update(map);
    } catch (IOException e) {
      throw new RuntimeException("get configuration from InputStream fail", e);
    }
  }
}
