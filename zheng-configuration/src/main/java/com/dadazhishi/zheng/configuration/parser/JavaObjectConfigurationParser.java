package com.dadazhishi.zheng.configuration.parser;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.IOException;
import java.util.Map;

public class JavaObjectConfigurationParser implements ConfigurationParser<Object> {

  private JavaPropsMapper javaPropsMapper = new JavaPropsMapper();

  @Override
  public Map<String, String> parse(Object content) {
    try {
      return javaPropsMapper.writeValueAsMap(content);
    } catch (IOException e) {
      throw new RuntimeException("parse object to map fail", e);
    }
  }
}
