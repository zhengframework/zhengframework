package com.dadazhishi.zheng.configuration.spi;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.InputStream;
import java.util.Map;

public interface AutoConfigurationParser extends ConfigurationParser<InputStream> {

  String[] fileTypes();

  default void init(Map<String, String> properties) {
  }
}
