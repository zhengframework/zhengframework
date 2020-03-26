package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonConfigurationParser implements AutoConfigurationParser {

  private JavaPropsMapper propsMapper = new JavaPropsMapper();
  private JsonMapper jsonMapper = new JsonMapper();

  @Override
  public Map<String, String> parse(InputStream content) {
    try {
      JsonNode jsonNode = jsonMapper.readTree(content);
      return propsMapper.writeValueAsMap(jsonNode);
    } catch (IOException e) {
      throw new RuntimeException("parse json fail", e);
    }
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".json"};
  }
}
