package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.AutoConfigurationParserSelector;
import com.dadazhishi.zheng.configuration.spi.AutoConfigurationSource;
import com.google.common.base.Joiner;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public class AutoConfigurationResolverSelector extends AbstractConfigurationResolver {

  private final AutoConfigurationParserSelector parserSelector;
  private final Map<String, AutoConfigurationSource> sourceMap;
  private Map<String, String> map;

  public AutoConfigurationResolverSelector(URI uri) {
    this(uri, Collections.emptyMap());
  }

  public AutoConfigurationResolverSelector(URI uri,
      Map<String, String> properties) {
    ServiceLoader<AutoConfigurationSource> autoConfigurationSources = ServiceLoader
        .load(AutoConfigurationSource.class);
    sourceMap = new HashMap<>();
    for (AutoConfigurationSource source : autoConfigurationSources) {
      source.init(properties);
      for (String scheme : source.schemes()) {
        sourceMap.put(scheme.toLowerCase(), source);
      }
    }

    parserSelector = new AutoConfigurationParserSelector(
        properties);

    load(uri);
  }

  private void load(URI uri) {
    String scheme = uri.getScheme();
    AutoConfigurationSource source1 = sourceMap.get(scheme);
    if (source1 == null) {
      throw new RuntimeException(
          "invalid schema, support schema: " + Joiner.on(",").join(sourceMap.keySet()));
    }
    Supplier<InputStream> source = source1.getSource(uri);
    InputStream inputStream = source.get();
    map = parserSelector.parse(uri, inputStream);
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }
}
