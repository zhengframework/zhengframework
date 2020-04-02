package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.tomlj.Toml;
import org.tomlj.TomlParseError;
import org.tomlj.TomlParseResult;

@Slf4j
public class TomlConfigurationParser implements AutoConfigurationParser {

  private boolean failOnError = false;

  public void init(Map<String, String> properties) {
    failOnError = Boolean.parseBoolean(properties.getOrDefault("failOnError", "false"));
  }

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
      if (failOnError) {
        throw new RuntimeException("parse toml fail", e);
      } else {
        log.warn("parse toml fail");
      }
    }
    return Collections.emptyMap();
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".toml" };
  }
}
