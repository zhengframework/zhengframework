package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.parser.PropertiesConfigurationParser;
import com.dadazhishi.zheng.configuration.resolver.ChainedConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.ClassPathConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.ConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.EnvironmentConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.FallbackConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.FileConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.InputStreamConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.MapConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.RefreshableFileConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.RefreshableInputStreamConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.RefreshableURLConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.SystemPropertiesConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.URLConfigurationResolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
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


  public ConfigurationBuilder withProperties(Supplier<InputStream> inputStream) {
    resolvers
        .add(new InputStreamConfigurationResolver(new PropertiesConfigurationParser(),
            inputStream));
    return this;
  }


  public ConfigurationBuilder withProperties(URL url) {
    resolvers
        .add(new URLConfigurationResolver(new PropertiesConfigurationParser(),
            url));
    return this;
  }

  public ConfigurationBuilder withRefreshableProperties(File path) {
    resolvers
        .add(new RefreshableFileConfigurationResolver(new PropertiesConfigurationParser(), path));
    return this;
  }

  public ConfigurationBuilder withRefreshableProperties(Supplier<InputStream> inputStream) {
    resolvers
        .add(new RefreshableInputStreamConfigurationResolver(new PropertiesConfigurationParser(),
            inputStream));
    return this;
  }

  public ConfigurationBuilder withRefreshableProperties(URL url) {
    resolvers
        .add(new RefreshableURLConfigurationResolver(new PropertiesConfigurationParser(),
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
