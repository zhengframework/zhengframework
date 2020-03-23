package com.dadazhishi.zheng.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;

public class JsonConfigurationSource implements ConfigurationSource {

  private final JavaPropsMapper propsMapper = new JavaPropsMapper();
  private final JsonMapper jsonMapper = new JsonMapper();
  private final Map<String, String> map;

  public JsonConfigurationSource(String json) throws IOException {
    JsonNode jsonNode = jsonMapper.readTree(json);
    this.map = propsMapper.writeValueAsMap(jsonNode);
  }

  public JsonConfigurationSource(File json) throws IOException {
    JsonNode jsonNode = jsonMapper.readTree(json);
    this.map = propsMapper.writeValueAsMap(jsonNode);
  }

  public JsonConfigurationSource(Reader json) throws IOException {
    JsonNode jsonNode = jsonMapper.readTree(json);
    this.map = propsMapper.writeValueAsMap(jsonNode);
  }

  public JsonConfigurationSource(InputStream json) throws IOException {
    JsonNode jsonNode = jsonMapper.readTree(json);
    this.map = propsMapper.writeValueAsMap(jsonNode);
  }

  @Override
  public Configuration getConfiguration() {
    return new ConfigurationImpl(Collections.unmodifiableMap(map));
  }
}
