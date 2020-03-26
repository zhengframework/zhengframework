package com.dadazhishi.zheng.configuration.spi;

import com.dadazhishi.zheng.configuration.source.ConfigurationSource;
import java.util.Map;

public interface AutoConfigurationSource extends ConfigurationSource {

  String[] schemes();

  default void init(Map<String, String> properties) {
  }

}
