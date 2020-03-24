package com.dadazhishi.zheng.configuration;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Configuration {

  Optional<String> get(String key);

  Set<String> keySet();

  Map<String, String> asMap();

  /**
   * 根据namespace获得静态Configuration实例
   */
  Configuration getConfiguration(String namespace);

  /**
   * 根据namespace获得静态Configuration实例
   */
  Set<Configuration> getConfigurationSet(String namespace);

  /**
   * 根据namespace获得静态Configuration实例
   */
  Map<String, Configuration> getConfigurationMap(String namespace);
}
