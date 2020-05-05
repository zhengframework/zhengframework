package com.github.zhengframework.configuration;

/*-
 * #%L
 * zheng-core
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
import com.github.zhengframework.configuration.source.ConfigurationSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public class SourceBasedConfiguration extends AbstractConfiguration {

  private final ConfigurationSource configurationSource;
  private final Environment environment;

  public SourceBasedConfiguration(
      ConfigurationSource configurationSource, Environment environment) {
    this.configurationSource = configurationSource;
    this.environment = environment;
  }

  @Override
  public Optional<String> get(String key) {
    Map<String, String> configuration = configurationSource.getConfiguration(environment);
    String value = configuration.get(key);
    if (value != null) {
      PlaceHolder placeHolder = new PlaceHolder(this);
      return Optional.ofNullable(placeHolder.replace(value));
    }
    return Optional.empty();
  }

  @Override
  public Set<String> keySet() {
    return configurationSource.getConfiguration(environment).keySet();
  }

  @Override
  public Map<String, String> asMap() {
    Map<String, String> configuration = configurationSource.getConfiguration(environment);
    Map<String, String> copy = new HashMap<>();
    PlaceHolder placeHolder = new PlaceHolder(this);
    for (Entry<String, String> entry : configuration.entrySet()) {
      copy.put(entry.getKey(), placeHolder.replace(entry.getValue()));
    }
    return copy;
  }

  @Override
  public String toString() {
    return asMap().toString();
  }
}
