package com.github.zhengframework.configuration;

/*-
 * #%L
 * zheng-configuration
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
    final CachedConfigurationSource cachedConfigurationSource =
        new CachedConfigurationSource(configurationSource);
    cachedConfigurationSource.init();

    Reloadable reloadable = () -> cachedConfigurationSource.reload(environment);
    //    reloadable.reload();
    reloadStrategy.register(reloadable);

    return new SourceBasedConfiguration(cachedConfigurationSource, environment);
  }
}
