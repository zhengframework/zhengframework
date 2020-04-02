package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefreshableInputStreamConfigurationResolver extends RefreshableConfigurationResolver {

  private final ConfigurationParser<InputStream> parser;

  private final Supplier<InputStream> inputStreamSupplier;

  private final boolean failOnError;

  public RefreshableInputStreamConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier) {
    this(parser, inputStreamSupplier, true);
  }

  public RefreshableInputStreamConfigurationResolver(
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier, boolean failOnError) {
    super();
    this.parser = parser;
    this.inputStreamSupplier = inputStreamSupplier;
    this.failOnError = failOnError;
  }

  public RefreshableInputStreamConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier) {
    this(initialDelay, delay, unit, parser, inputStreamSupplier, true);
  }

  public RefreshableInputStreamConfigurationResolver(long initialDelay, long delay,
      TimeUnit unit,
      ConfigurationParser<InputStream> parser,
      Supplier<InputStream> inputStreamSupplier, boolean failOnError) {
    super(initialDelay, delay, unit);
    this.parser = parser;
    this.inputStreamSupplier = inputStreamSupplier;
    this.failOnError = failOnError;
  }

  @Override
  public void doUpdate() {
    try (InputStream inputStream = inputStreamSupplier.get()) {
      Map<String, String> map = parser.parse(inputStream);
      update(map);
    } catch (IOException e) {
      if (failOnError) {
        throw new RuntimeException("read configuration from InputStream fail", e);
      } else {
        log.warn("read configuration from InputStream fail");
      }

    }
  }
}
