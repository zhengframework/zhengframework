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

import static com.github.zhengframework.configuration.source.EnvironmentVariablesConfigurationSource.convertToPropertiesKey;
import static com.github.zhengframework.configuration.source.EnvironmentVariablesConfigurationSource.formatEnvironmentContext;

import com.github.zhengframework.configuration.environment.Environment;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class InMemoryConfigurationSource extends AbstractConfigurationSource {

  private final Map<String, String> map;

  public InMemoryConfigurationSource(Map<String, String> map) {
    this.map = Collections.unmodifiableMap(Objects.requireNonNull(map));
  }

  @Override
  public void init() {
  }

  @Override
  protected Map<String, String> getConfigurationInternal(Environment environment) {
    String environmentContext = formatEnvironmentContext(environment);
    Map<String, String> copyMap = new HashMap<>();
    for (Entry<String, String> entry : map.entrySet()) {
      if (entry.getKey().startsWith(environmentContext)) {
        copyMap.put(convertToPropertiesKey(entry.getKey(), environmentContext), entry.getValue());
      }
    }
    return Collections.unmodifiableMap(copyMap);
  }
}
