package com.github.zhengframework.configuration.source;

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

import com.github.zhengframework.configuration.environment.Environment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MergeConfigurationSource implements ConfigurationSource {

  private final List<ConfigurationSource> sources = new ArrayList<>();

  public MergeConfigurationSource(ConfigurationSource... sources) {
    Objects.requireNonNull(sources);
    for (ConfigurationSource source : sources) {
      MergeConfigurationSource.this.sources.add(Objects.requireNonNull(source));
    }
  }

  public MergeConfigurationSource(Iterator<ConfigurationSource> sources) {
    Objects.requireNonNull(sources)
        .forEachRemaining(
            configurationSource ->
                MergeConfigurationSource.this.sources.add(
                    Objects.requireNonNull(configurationSource)));
  }

  public MergeConfigurationSource(Iterable<ConfigurationSource> sources) {
    Objects.requireNonNull(sources)
        .forEach(
            configurationSource ->
                MergeConfigurationSource.this.sources.add(
                    Objects.requireNonNull(configurationSource)));
  }

  @Override
  public void init() {
    for (ConfigurationSource source : sources) {
      source.init();
    }
  }

  @Override
  public void addListener(ConfigurationSourceListener listener) {
    for (ConfigurationSource source : sources) {
      source.addListener(listener);
    }
  }

  @Override
  public void removeListener(ConfigurationSourceListener listener) {
    for (ConfigurationSource source : sources) {
      source.removeListener(listener);
    }
  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    Map<String, String> map = new HashMap<>();
    for (ConfigurationSource source : sources) {
      Map<String, String> configuration = source.getConfiguration(environment);
      map.putAll(configuration);
    }
    return map;
  }
}
