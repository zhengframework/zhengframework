package com.github.zhengframework.configuration.parser;

import com.github.zhengframework.configuration.ex.ConfigurationSourceException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

public class INIConfigurationParser implements ConfigurationParser, FileConfigurationParser {

  @Override
  public Map<String, String> parse(InputStream inputStream) {
    try {
      Map<String, String> map = new HashMap<>();
      Ini ini = new Ini(inputStream);
      for (Section section : ini.values()) {
        for (String s : section.keySet()) {
          map.put(section.getName() + "." + s, section.fetch(s));
        }
      }
      return map;
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from inputStream", e);
    }
  }

  @Override
  public String[] supportFileTypes() {
    return new String[]{".properties"};
  }

  @Override
  public Map<String, String> parse(String fileName, InputStream inputStream) {
    checkSupportFileTypes(fileName);
    try {
      Map<String, String> map = new HashMap<>();
      Ini ini = new Ini(inputStream);
      for (Section section : ini.values()) {
        for (String s : section.keySet()) {
          map.put(section.getName() + "." + s, section.fetch(s));
        }
      }
      return map;
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from file: " + fileName, e);
    }
  }
}
