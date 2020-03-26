package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlConfigurationParser implements AutoConfigurationParser {

  private JavaPropsMapper propsMapper = new JavaPropsMapper();
  private YAMLMapper yamlMapper = new YAMLMapper();

  @Override
  public Map<String, String> parse(InputStream content) {
    try {
      JsonNode jsonNode = yamlMapper.readTree(content);
      return propsMapper.writeValueAsMap(jsonNode);
    } catch (IOException e) {
      throw new RuntimeException("parse yaml fail", e);
    }
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".yml", ".yaml"};
  }
}
