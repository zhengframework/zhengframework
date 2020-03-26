package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.tomlj.Toml;
import org.tomlj.TomlParseError;
import org.tomlj.TomlParseResult;

public class TomlConfigurationParser implements AutoConfigurationParser {

  @Override
  public Map<String, String> parse(InputStream content) {
    try {
      Map<String, String> map = new HashMap<>();
      TomlParseResult tomlParseResult = Toml.parse(content);
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
      throw new RuntimeException("parse toml fail", e);
    }
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".toml"};
  }
}
