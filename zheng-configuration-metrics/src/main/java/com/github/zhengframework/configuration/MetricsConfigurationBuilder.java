package com.github.zhengframework.configuration;

/*-
 * #%L
 * zheng-configuration-metrics
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static java.util.Objects.requireNonNull;

import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.configuration.environment.DefaultEnvironment;
import com.github.zhengframework.configuration.environment.Environment;
import com.github.zhengframework.configuration.reload.CachedConfigurationSource;
import com.github.zhengframework.configuration.reload.ImmediateReloadStrategy;
import com.github.zhengframework.configuration.reload.MeteredReloadable;
import com.github.zhengframework.configuration.reload.ReloadStrategy;
import com.github.zhengframework.configuration.reload.Reloadable;
import com.github.zhengframework.configuration.source.ConfigurationSource;
import com.github.zhengframework.configuration.source.EmptyConfigurationSource;
import com.github.zhengframework.configuration.source.MeteredConfigurationSource;

public class MetricsConfigurationBuilder {

  private ConfigurationSource configurationSource;
  private ReloadStrategy reloadStrategy;
  private Environment environment;
  private MetricRegistry metricRegistry;
  private String prefix;

  public MetricsConfigurationBuilder() {
    configurationSource = new EmptyConfigurationSource();
    reloadStrategy = new ImmediateReloadStrategy();
    environment = new DefaultEnvironment();
    prefix = "";
  }

  public static MetricsConfigurationBuilder builder() {
    return new MetricsConfigurationBuilder();
  }

  public MetricsConfigurationBuilder withConfigurationSource(
      ConfigurationSource configurationSource) {
    this.configurationSource = configurationSource;
    return this;
  }

  public MetricsConfigurationBuilder withReloadStrategy(ReloadStrategy reloadStrategy) {
    this.reloadStrategy = reloadStrategy;
    return this;
  }

  public MetricsConfigurationBuilder withEnvironment(Environment environment) {
    this.environment = environment;
    return this;
  }

  public MetricsConfigurationBuilder withMetrics(MetricRegistry metricRegistry, String prefix) {
    this.prefix = requireNonNull(prefix);
    this.metricRegistry = metricRegistry;
    return this;
  }

  public Configuration build() {
    final CachedConfigurationSource cachedConfigurationSource = new CachedConfigurationSource(
        configurationSource);
    if (metricRegistry != null) {
      configurationSource = new MeteredConfigurationSource(metricRegistry, prefix,
          cachedConfigurationSource);
    }
    cachedConfigurationSource.init();

    Reloadable reloadable = () -> cachedConfigurationSource.reload(environment);

    if (metricRegistry != null) {
      reloadable = new MeteredReloadable(metricRegistry, prefix, reloadable);
    }
    reloadable.reload();
    reloadStrategy.register(reloadable);

    SourceBasedConfiguration configuration = new SourceBasedConfiguration(cachedConfigurationSource,
        environment);
    if (metricRegistry != null) {
      return new MeteredConfiguration(metricRegistry, prefix, configuration);
    }
    return configuration;
  }

}
