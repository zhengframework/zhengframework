package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

public class INIConfigurationParser implements AutoConfigurationParser {

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
      throw new RuntimeException("parse ini fail", e);
    }
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".ini"};
  }
}
