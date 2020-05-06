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

import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfigurationValueProvider implements Provider<String> {

  private static final Logger log = LoggerFactory.getLogger(ConfigurationValueProvider.class);
  private final String key;
  private Configuration configuration;

  ConfigurationValueProvider(Configuration configuration, String key) {
    this.key = key;
    this.configuration = configuration;
  }

  @Override
  public String get() {
    log.debug("get value by key=[{}]", key);
    return configuration.get(key).orElse(null);
  }
}
