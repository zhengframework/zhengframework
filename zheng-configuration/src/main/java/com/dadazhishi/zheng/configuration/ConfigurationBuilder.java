package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.ex.ConfigurationException;
import com.dadazhishi.zheng.configuration.io.FileLocator;
import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import com.dadazhishi.zheng.configuration.parser.JavaObjectConfigurationParser;
import com.dadazhishi.zheng.configuration.parser.JsonConfigurationParser;
import com.dadazhishi.zheng.configuration.parser.PropertiesConfigurationParser;
import com.dadazhishi.zheng.configuration.parser.YamlConfigurationParser;
import com.dadazhishi.zheng.configuration.resolver.AutoFileConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.CombinedConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.ConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.EnvironmentConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.FallbackConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.FileConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.InputStreamConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.MapConfigurationResolver;
import com.dadazhishi.zheng.configuration.resolver.Reloadable;
import com.dadazhishi.zheng.configuration.resolver.ReloadingTrigger;
import com.dadazhishi.zheng.configuration.resolver.SystemConfigurationResolver;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.apache.commons.collections4.map.HashedMap;

public class ConfigurationBuilder {

  private final List<ConfigurationResolver> resolvers = Lists.newArrayList();
  private FallbackConfigurationResolver fallback;
  private long delay;
  private TimeUnit unit;
  private Map<Reloadable, ReloadingTrigger> reloadingTriggerMap = new HashedMap<>();

  ConfigurationBuilder() {
  }

  public static ConfigurationBuilder create() {
    return new ConfigurationBuilder();
  }

  public Map<Reloadable, ReloadingTrigger> getReloadingTriggerMap() {
    return reloadingTriggerMap;
  }

  public ConfigurationBuilder withDefaultReloadingTrigger(long delay, TimeUnit unit) {
    this.delay = delay;
    this.unit = unit;
    Preconditions.checkArgument(unit != null, "TimeUnit is null");
    Preconditions.checkArgument(delay <= 0, "delay must greater than zero");
    return this;
  }

  public ConfigurationBuilder withEnvironmentVariables() {
    resolvers.add(new EnvironmentConfigurationResolver());
    return this;
  }

  public ConfigurationBuilder withSystemProperties() {
    resolvers.add(new SystemConfigurationResolver());
    return this;
  }

  public ConfigurationBuilder withMap(Map<String, String> map) {
    resolvers.add(new MapConfigurationResolver(map));
    return this;
  }

  public ConfigurationBuilder withProperties(Properties properties) {
    return withMap(Maps.fromProperties(properties));
  }

  public ConfigurationBuilder withObject(Object object) {
    Preconditions.checkArgument(object != null);
    JavaObjectConfigurationParser parser = new JavaObjectConfigurationParser();
    Map<String, String> map = parser.parse(object);
    return withMap(map);
  }

  public ConfigurationBuilder withFile(FileLocator fileLocator,
      ConfigurationParser<InputStream> configurationParser) {
    resolvers.add(new FileConfigurationResolver(fileLocator,
        configurationParser));
    return this;
  }

  public ConfigurationBuilder withProperties(String path) {
    return withProperties(FileLocator.builder()
        .fileName(path)
        .build());
  }

  public ConfigurationBuilder withYaml(String path) {
    return withYaml(FileLocator.builder()
        .fileName(path)
        .build());
  }

  public ConfigurationBuilder withJson(String path) {
    return withJson(FileLocator.builder()
        .fileName(path)
        .build());
  }

  public ConfigurationBuilder withProperties(FileLocator fileLocator) {
    return withFile(fileLocator, new PropertiesConfigurationParser());
  }

  public ConfigurationBuilder withJson(FileLocator fileLocator) {
    return withFile(fileLocator, new JsonConfigurationParser());
  }

  public ConfigurationBuilder withYaml(FileLocator fileLocator) {
    return withFile(fileLocator, new YamlConfigurationParser());
  }

  public ConfigurationBuilder withProperties(Supplier<InputStream> inputStream) {
    resolvers
        .add(new InputStreamConfigurationResolver(new PropertiesConfigurationParser(),
            inputStream));
    return this;
  }

  public ConfigurationBuilder withYaml(Supplier<InputStream> inputStream) {
    resolvers
        .add(new InputStreamConfigurationResolver(new YamlConfigurationParser(),
            inputStream));
    return this;
  }

  public ConfigurationBuilder withJson(Supplier<InputStream> inputStream) {
    resolvers
        .add(new InputStreamConfigurationResolver(new JsonConfigurationParser(),
            inputStream));
    return this;
  }

  public ConfigurationBuilder withURI(URI uri) {
    try {
      FileLocator fileLocator = FileLocator.builder().sourceURL(uri.toURL()).build();
      resolvers.add(new AutoFileConfigurationResolver(fileLocator, true,
          Collections.singletonMap("failOnError", "false")));
    } catch (MalformedURLException e) {
      throw new ConfigurationException(e);
    }
    return this;
  }

  public ConfigurationBuilder withURI(String uri) {
    return withURI(URI.create(uri));
  }

  public ConfigurationBuilder withResolver(ConfigurationResolver resolver) {
    resolvers.add(resolver);
    return this;
  }

  public ConfigurationBuilder withResolver(ConfigurationResolver resolver, long delay,
      TimeUnit unit) {
    resolvers.add(resolver);
    if (resolver instanceof Reloadable) {
      Reloadable reloadable = (Reloadable) resolver;
      Preconditions.checkArgument(unit != null, "TimeUnit is null");
      Preconditions.checkArgument(delay <= 0, "delay must greater than zero");
      ReloadingTrigger reloadingTrigger = new ReloadingTrigger(reloadable, delay, unit);
      reloadingTriggerMap.put(reloadable, reloadingTrigger);
    }
    return this;
  }

  public ConfigurationBuilder withFallback(FallbackConfigurationResolver fallback) {
    this.fallback = fallback;
    return this;
  }

  public Configuration build() {
    if (unit != null) {
      for (ConfigurationResolver resolver : resolvers) {
        if (resolver instanceof Reloadable) {
          Reloadable reloadable = (Reloadable) resolver;
          if (!reloadingTriggerMap.containsKey(reloadable)) {
            ReloadingTrigger reloadingTrigger = new ReloadingTrigger(reloadable, delay, unit);
            reloadingTriggerMap.put(reloadable, reloadingTrigger);
          }
        }
      }
    }
    if (!reloadingTriggerMap.isEmpty()) {
      for (ReloadingTrigger trigger : reloadingTriggerMap.values()) {
        trigger.start();
      }
    }
    if (fallback != null) {
      return new ConfigurationImpl(
          new FallbackConfigurationResolver(new CombinedConfigurationResolver(resolvers),
              fallback));
    }
    return new ConfigurationImpl(new CombinedConfigurationResolver(resolvers));
  }

}
