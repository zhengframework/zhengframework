package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YamlConfigurationParser implements AutoConfigurationParser {

  private JavaPropsMapper propsMapper = new JavaPropsMapper();
  private YAMLMapper yamlMapper = new YAMLMapper();
  private boolean failOnError = false;

  public void init(Map<String, String> properties) {
    failOnError = Boolean.parseBoolean(properties.getOrDefault("failOnError", "false"));
  }

  @Override
  public Map<String, String> parse(InputStream content) {
    try {
      JsonNode jsonNode = yamlMapper.readTree(content);
      return propsMapper.writeValueAsMap(jsonNode);
    } catch (IOException e) {
      if (failOnError) {
        throw new RuntimeException("parse yaml fail", e);
      } else {
        log.warn("parse yaml fail");
      }
    }
    return Collections.emptyMap();
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".yml", ".yaml"};
  }
}
