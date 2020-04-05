package com.dadazhishi.zheng.configuration.resolver;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;

public class EnvironmentConfigurationResolver extends ReloadableConfigurationResolver {

  private static Map<String, String> transformEnvMap(Map<String, String> envMap) {
    Map<String, String> map = Maps.newHashMapWithExpectedSize(envMap.size());
    for (Map.Entry<String, String> entry : envMap.entrySet()) {
      map.put(envToKey(entry.getKey()), entry.getValue());
    }
    return Collections.unmodifiableMap(map);
  }

  private static String envToKey(String env) {
    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, env).replace('-', '.');
  }

  @Override
  public void reload() {
    update(transformEnvMap(System.getenv()));
  }
}
