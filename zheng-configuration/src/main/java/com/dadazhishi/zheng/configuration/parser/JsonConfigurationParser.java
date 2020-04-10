package com.github.zhengframework.configuration.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.github.zhengframework.configuration.ex.ConfigurationSourceException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonConfigurationParser implements ConfigurationParser, FileConfigurationParser {

  private JavaPropsMapper propsMapper = new JavaPropsMapper();
  private JsonMapper jsonMapper = new JsonMapper();

  @Override
  public Map<String, String> parse(InputStream inputStream) {
    try {
      JsonNode jsonNode = jsonMapper.readTree(inputStream);
      return propsMapper.writeValueAsMap(jsonNode);
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from inputStream", e);
    }
  }

  @Override
  public String[] supportFileTypes() {
    return new String[]{".json"};
  }

  @Override
  public Map<String, String> parse(String fileName, InputStream inputStream) {
    checkSupportFileTypes(fileName);
    try {
      JsonNode jsonNode = jsonMapper.readTree(inputStream);
      return propsMapper.writeValueAsMap(jsonNode);
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from file: " + fileName, e);
    }
  }
}
