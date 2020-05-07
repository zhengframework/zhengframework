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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class EnvironmentVariablesConfigurationSource extends AbstractConfigurationSource {

  private static final char ENV_DELIMITER = '_';
  private static final char PROPERTIES_DELIMITER = '.';

  static String convertToPropertiesKey(String environmentVariableKey, String environmentContext) {
    return environmentVariableKey
        .substring(environmentContext.length())
        .replace(ENV_DELIMITER, PROPERTIES_DELIMITER);
  }

  static String formatEnvironmentContext(Environment environment) {
    String environmentName = environment.getName();
    if (StringUtils.isEmpty(environmentName)) {
      return "";
    } else {
      return environmentName.endsWith("_") ? environmentName : environmentName + "_";
    }
  }

  @Override
  protected Map<String, String> getConfigurationInternal(Environment environment) {
    Map<String, String> copyMap = new HashMap<>();
    String environmentContext = formatEnvironmentContext(environment);
    for (Entry<String, String> entry : System.getenv().entrySet()) {
      if (entry.getKey().startsWith(environmentContext)) {
        copyMap.put(convertToPropertiesKey(entry.getKey(), environmentContext), entry.getValue());
      }
    }
    return copyMap;
  }

  @Override
  public void init() {}
}
