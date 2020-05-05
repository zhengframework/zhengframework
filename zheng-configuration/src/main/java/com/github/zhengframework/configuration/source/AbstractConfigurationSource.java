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
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractConfigurationSource implements ConfigurationSource {

  private Map<String, Map<String, String>> oldConfig = new HashMap<>();
  private List<ConfigurationSourceListener> listenerList = new ArrayList<>();

  @Override
  public void addListener(ConfigurationSourceListener listener) {
    listenerList.add(Objects.requireNonNull(listener));
  }

  @Override
  public void removeListener(ConfigurationSourceListener listener) {
    listenerList.remove(Objects.requireNonNull(listener));
  }

  private void fireEvent(String environmentName, Map<String, String> newConfig) {
    Map<String, String> map = oldConfig.getOrDefault(environmentName, Collections.emptyMap());
    MapDifference<String, String> difference = Maps.difference(map, newConfig);
    oldConfig.put(environmentName, newConfig);
    if (!difference.areEqual()) {
      for (Entry<String, ValueDifference<String>> entry : difference
          .entriesDiffering().entrySet()) {
        for (ConfigurationSourceListener listener : listenerList) {
          if (listener.accept(entry.getKey())) {
            ValueDifference<String> valueDifference = entry.getValue();
            listener.onChanged(entry.getKey(), valueDifference.leftValue(),
                valueDifference.rightValue());
          }
        }
      }
    }
  }

  protected abstract Map<String, String> getConfigurationInternal(Environment environment);

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    final Map<String, String> newConfig = getConfigurationInternal(environment);
    fireEvent(StringUtils.trimToEmpty(environment.getName()), newConfig);
    return newConfig;
  }
}
