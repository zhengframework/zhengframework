package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.ex.ConfigurationSourceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlConfigurationParser implements ConfigurationParser, FileConfigurationParser {

  private JavaPropsMapper propsMapper = new JavaPropsMapper();
  private YAMLMapper yamlMapper = new YAMLMapper();

  @Override
  public Map<String, String> parse(InputStream inputStream) {
    try {
      JsonNode jsonNode = yamlMapper.readTree(inputStream);
      return propsMapper.writeValueAsMap(jsonNode);
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from inputStream", e);
    }
  }

  @Override
  public String[] supportFileTypes() {
    return new String[]{".yml", ".yaml"};
  }

  @Override
  public Map<String, String> parse(String fileName, InputStream inputStream) {
    checkSupportFileTypes(fileName);
    try {
      JsonNode jsonNode = yamlMapper.readTree(inputStream);
      return propsMapper.writeValueAsMap(jsonNode);
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from file: " + fileName, e);
    }
  }

}
