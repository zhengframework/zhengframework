package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.parser.PropertiesConfigurationParser;
import com.dadazhishi.zheng.configuration.resolver.AutoConfigurationResolverSelector;
import com.dadazhishi.zheng.configuration.resolver.ChainedConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.ClassPathConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.ConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.EnvironmentConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.FallbackConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.FileConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.HttpConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.InputStreamConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.MapConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.PathConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.SystemPropertiesConfigurationResolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public class ConfigurationBuilder {

  private final List<ConfigurationResolver> resolvers = Lists.newArrayList();
  private FallbackConfigurationResolver fallback;

  ConfigurationBuilder() {
  }

  public static ConfigurationBuilder create() {
    return new ConfigurationBuilder();
  }

  public ConfigurationBuilder withEnvironmentVariables() {
    resolvers.add(new EnvironmentConfigurationResolver());
    return this;
  }

  public ConfigurationBuilder withSystemProperties() {
    resolvers.add(new SystemPropertiesConfigurationResolver());
    return this;
  }

  public ConfigurationBuilder withMap(Map<String, String> map) {
    resolvers.add(new MapConfigurationResolver(map));
    return this;
  }

  public ConfigurationBuilder withProperties(Properties properties) {
    return withMap(Maps.fromProperties(properties));
  }

  public ConfigurationBuilder withClassPathProperties(String path) {
    resolvers
        .add(new ClassPathConfigurationResolver(new PropertiesConfigurationParser(),
            path));
    return this;
  }

  public ConfigurationBuilder withProperties(File path) {
    resolvers
        .add(new FileConfigurationResolver(new PropertiesConfigurationParser(), path));
    return this;
  }

  public ConfigurationBuilder withProperties(Path path) {
    resolvers
        .add(new PathConfigurationResolver(new PropertiesConfigurationParser(), path));
    return this;
  }

  public ConfigurationBuilder withProperties(Supplier<InputStream> inputStream) {
    resolvers
        .add(new InputStreamConfigurationResolver(new PropertiesConfigurationParser(),
            inputStream));
    return this;
  }

  public ConfigurationBuilder with(URI uri) {
    resolvers.add(new AutoConfigurationResolverSelector(uri,
        Collections.singletonMap("failOnError", "false")));
    return this;
  }

  public ConfigurationBuilder with(URI uri, Map<String, String> properties) {
    resolvers.add(new AutoConfigurationResolverSelector(uri, properties));
    return this;
  }

  public ConfigurationBuilder with(String uri) {
    resolvers.add(new AutoConfigurationResolverSelector(URI.create(uri),
        Collections.singletonMap("failOnError", "false")));
    return this;
  }

  public ConfigurationBuilder with(String uri, Map<String, String> properties) {
    resolvers.add(new AutoConfigurationResolverSelector(URI.create(uri), properties));
    return this;
  }

  public ConfigurationBuilder withProperties(URL url) {
    resolvers
        .add(new HttpConfigurationResolver(new PropertiesConfigurationParser(),
            url));
    return this;
  }

  public ConfigurationBuilder withResolver(ConfigurationResolver resolver) {
    resolvers.add(resolver);
    return this;
  }

  public ConfigurationBuilder withFallback(FallbackConfigurationResolver fallback) {
    this.fallback = fallback;
    return this;
  }

  public Configuration build() {
    if (fallback != null) {
      return new ConfigurationImpl(
          new FallbackConfigurationResolver(new ChainedConfigurationResolver(resolvers),
              fallback));
    }
    return new ConfigurationImpl(new ChainedConfigurationResolver(resolvers));
  }

}
