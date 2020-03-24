package com.dadazhishi.zheng.configuration.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonConfigurationParser implements ConfigurationParser<InputStream> {

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
}
