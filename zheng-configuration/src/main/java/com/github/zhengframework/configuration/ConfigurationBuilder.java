package com.github.zhengframework.configuration;

import com.github.zhengframework.configuration.environment.DefaultEnvironment;
import com.github.zhengframework.configuration.environment.Environment;
import com.github.zhengframework.configuration.reload.CachedConfigurationSource;
import com.github.zhengframework.configuration.reload.ImmediateReloadStrategy;
import com.github.zhengframework.configuration.reload.ReloadStrategy;
import com.github.zhengframework.configuration.reload.Reloadable;
import com.github.zhengframework.configuration.source.ConfigurationSource;
import com.github.zhengframework.configuration.source.EmptyConfigurationSource;

public class ConfigurationBuilder {

  private ConfigurationSource configurationSource;
  private ReloadStrategy reloadStrategy;
  private Environment environment;

  public ConfigurationBuilder() {
    configurationSource = new EmptyConfigurationSource();
    reloadStrategy = new ImmediateReloadStrategy();
    environment = new DefaultEnvironment();
  }

  public static ConfigurationBuilder builder() {
    return new ConfigurationBuilder();
  }

  public ConfigurationBuilder withConfigurationSource(ConfigurationSource configurationSource) {
    this.configurationSource = configurationSource;
    return this;
  }

  public ConfigurationBuilder withReloadStrategy(ReloadStrategy reloadStrategy) {
    this.reloadStrategy = reloadStrategy;
    return this;
  }

  public ConfigurationBuilder withEnvironment(Environment environment) {
    this.environment = environment;
    return this;
  }

  public Configuration build() {
    final CachedConfigurationSource cachedConfigurationSource = new CachedConfigurationSource(
        configurationSource);
    cachedConfigurationSource.init();

    Reloadable reloadable = () -> cachedConfigurationSource.reload(environment);
    reloadable.reload();
    reloadStrategy.register(reloadable);

    return new SimpleConfiguration(cachedConfigurationSource, environment);
  }

}
