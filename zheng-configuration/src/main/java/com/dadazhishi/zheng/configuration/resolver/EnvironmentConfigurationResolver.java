package com.dadazhishi.zheng.configuration.resolver;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class EnvironmentConfigurationResolver extends AbstractConfigurationResolver {

  private final Map<String, String> envMap;

  public EnvironmentConfigurationResolver() {
    this.envMap = transformEnvMap(System.getenv());
  }

  private static Map<String, String> transformEnvMap(Map<String, String> envMap) {
    Map<String, String> map = Maps.newHashMapWithExpectedSize(envMap.size());
    for (Map.Entry<String, String> entry : envMap.entrySet()) {
      map.put(envToKey(entry.getKey()), entry.getValue());
//      map.put(entry.getKey(), entry.getValue());
    }
    return map;
  }

  private static String envToKey(String env) {
    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, env).replace('-', '.');
  }

  @Override
  protected Map<String, String> delegate() {
    return envMap;
  }

  @Override
  public Set<String> keySet() {
    return Collections.emptySet();
  }
}
