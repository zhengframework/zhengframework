package com.dadazhishi.zheng.configuration.parser;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

public class INIConfigurationParserTest {

  @org.junit.Test
  public void parse() {
    INIConfigurationParser parser = new INIConfigurationParser();
    InputStream content = INIConfigurationParserTest.class.getResourceAsStream("/food.ini");
    Map<String, String> map = parser
        .parse(content);
    for (Entry<String, String> entry : map.entrySet()) {
      System.out.println(entry.getKey() + "=" + entry.getValue());
    }

  }
}