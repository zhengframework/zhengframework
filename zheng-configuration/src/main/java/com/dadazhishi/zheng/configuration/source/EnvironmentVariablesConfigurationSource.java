package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.environment.Environment;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class EnvironmentVariablesConfigurationSource implements ConfigurationSource {

  private final static char ENV_DELIMITER = '_';
  private final static char PROPERTIES_DELIMITER = '.';


  static String convertToPropertiesKey(String environmentVariableKey,
      String environmentContext) {
    return environmentVariableKey.substring(environmentContext.length())
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
  public Map<String, String> getConfiguration(Environment environment) {
    Map<String, String> copyMap = new HashMap<>();
    String environmentContext = formatEnvironmentContext(environment);
    for (Entry<String, String> entry : System.getenv().entrySet()) {
      if (entry.getKey().startsWith(environmentContext)) {
        copyMap
            .put(convertToPropertiesKey(entry.getKey(), environmentContext), entry.getValue());
      }
    }
    return copyMap;
  }

  @Override
  public void init() {
  }
}
