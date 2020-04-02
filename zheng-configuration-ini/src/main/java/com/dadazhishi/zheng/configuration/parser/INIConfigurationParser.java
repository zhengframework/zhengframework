package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

@Slf4j
public class INIConfigurationParser implements AutoConfigurationParser {

  private boolean failOnError = false;

  public void init(Map<String, String> properties) {
    failOnError = Boolean.parseBoolean(properties.getOrDefault("failOnError", "false"));
  }

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  @Override
  public Map<String, String> parse(InputStream content) {
    try {
      Map<String, String> map = new HashMap<>();
      Ini ini = new Ini(content);
      for (Section section : ini.values()) {
        for (String s : section.keySet()) {
          map.put(section.getName() + "." + s, section.fetch(s));
        }
      }
      return map;
    } catch (IOException e) {
      if (failOnError) {
        throw new RuntimeException("parse ini fail", e);
      } else {
        log.warn("parse ini fail");
      }
    }
    return Collections.emptyMap();
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".ini" };
  }
}
