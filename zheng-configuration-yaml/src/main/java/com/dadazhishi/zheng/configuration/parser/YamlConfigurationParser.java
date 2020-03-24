package com.dadazhishi.zheng.configuration.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlConfigurationParser implements ConfigurationParser<InputStream> {

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
}
