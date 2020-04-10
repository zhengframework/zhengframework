package com.github.zhengframework.configuration.parser;

import com.github.zhengframework.configuration.ex.ConfigurationSourceException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.tomlj.Toml;
import org.tomlj.TomlParseError;
import org.tomlj.TomlParseResult;

public class TomlConfigurationParser implements ConfigurationParser, FileConfigurationParser {

  @Override
  public Map<String, String> parse(InputStream inputStream) {
    try {
      Map<String, String> map = new HashMap<>();
      TomlParseResult tomlParseResult = Toml.parse(inputStream);
      if (tomlParseResult.hasErrors()) {
        for (TomlParseError error : tomlParseResult.errors()) {
          throw error;
        }
      }
      for (String s : tomlParseResult.dottedKeySet(false)) {
        Object o = tomlParseResult.get(s);
        map.put(s, String.valueOf(o));
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
      TomlParseResult tomlParseResult = Toml.parse(inputStream);
      if (tomlParseResult.hasErrors()) {
        for (TomlParseError error : tomlParseResult.errors()) {
          throw error;
        }
      }
      for (String s : tomlParseResult.dottedKeySet(false)) {
        Object o = tomlParseResult.get(s);
        map.put(s, String.valueOf(o));
      }
      return map;
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from file: " + fileName, e);
    }
  }
}
