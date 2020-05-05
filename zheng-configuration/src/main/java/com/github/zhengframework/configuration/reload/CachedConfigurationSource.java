package com.github.zhengframework.configuration.reload;

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

import static java.util.Objects.requireNonNull;

import com.github.zhengframework.configuration.environment.Environment;
import com.github.zhengframework.configuration.ex.MissingEnvironmentException;
import com.github.zhengframework.configuration.source.AbstractConfigurationSource;
import com.github.zhengframework.configuration.source.ConfigurationSource;
import java.util.HashMap;
import java.util.Map;

public class CachedConfigurationSource extends AbstractConfigurationSource {

  private final Map<String, Map<String, String>> cachedConfigurationPerEnvironment;
  private final ConfigurationSource underlyingSource;

  public CachedConfigurationSource(ConfigurationSource underlyingSource) {
    this.underlyingSource = requireNonNull(underlyingSource);

    cachedConfigurationPerEnvironment = new HashMap<>();
  }

  @Override
  protected Map<String, String> getConfigurationInternal(Environment environment) {
    if (cachedConfigurationPerEnvironment.containsKey(environment.getName())) {
      return cachedConfigurationPerEnvironment.get(environment.getName());
    } else {
      throw new MissingEnvironmentException(environment.getName());
    }
  }

  @Override
  public void init() {
    underlyingSource.init();
  }

  public void reload(Environment environment) {
    Map<String, String> configuration = underlyingSource.getConfiguration(environment);
    cachedConfigurationPerEnvironment.put(environment.getName(), configuration);
  }
}
