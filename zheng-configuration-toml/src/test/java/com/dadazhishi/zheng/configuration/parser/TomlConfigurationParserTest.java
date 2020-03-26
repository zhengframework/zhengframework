package com.dadazhishi.zheng.configuration.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;

public class TomlConfigurationParserTest {

  @Test
  public void parse() throws IOException {
    InputStream stream = TomlConfigurationParserTest.class
        .getResourceAsStream("/test.toml");
    TomlConfigurationParser parser = new TomlConfigurationParser();
    Map<String, String> map = parser.parse(stream);
    for (Entry<String, String> entry : map.entrySet()) {
      System.out.println(entry.getKey() + "=" + entry.getValue());
    }

  }
}