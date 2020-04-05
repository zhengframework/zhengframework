package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonConfigurationParser implements AutoConfigurationParser {

  private JavaPropsMapper propsMapper = new JavaPropsMapper();
  private JsonMapper jsonMapper = new JsonMapper();
  private boolean failOnError = false;

  public void init(Map<String, String> properties) {
    failOnError = Boolean.parseBoolean(properties.getOrDefault("failOnError", "false"));
  }

  @Override
  public Map<String, String> parse(InputStream content) {
    try {
      JsonNode jsonNode = jsonMapper.readTree(content);
      return propsMapper.writeValueAsMap(jsonNode);
    } catch (IOException e) {
      if (failOnError) {
        throw new RuntimeException("parse json fail", e);
      } else {
        log.warn("parse json fail");
      }
    }
    return Collections.emptyMap();
  }

  @Override
  public String[] fileTypes() {
    return new String[]{".json" };
  }
}
