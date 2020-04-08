package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.environment.Environment;
import java.util.Collections;
import java.util.Map;

public class EmptyConfigurationSource implements ConfigurationSource {

  private final Map<String, String> map = Collections.emptyMap();

  @Override
  public void init() {

  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    return map;
  }
}
