package com.dadazhishi.zheng.configuration;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class YamlConfigurationSource implements ConfigurationSource {

  private final JavaPropsMapper propsMapper = new JavaPropsMapper();
  private final Yaml yamlMapper = new Yaml();
  private final Map<String, String> map;

  public YamlConfigurationSource(String yaml) throws IOException {
    Map<String, Object> o = yamlMapper.load(yaml);
    map = propsMapper.writeValueAsMap(o);
  }

  public YamlConfigurationSource(File yaml) throws IOException {
    Map<String, Object> o = yamlMapper.load(new FileReader(yaml));
    map = propsMapper.writeValueAsMap(o);
  }

  public YamlConfigurationSource(InputStream yaml) throws IOException {
    Map<String, Object> o = yamlMapper.load(yaml);
    map = propsMapper.writeValueAsMap(o);
  }

  public YamlConfigurationSource(Reader yaml) throws IOException {
    Map<String, Object> o = yamlMapper.load(yaml);
    map = propsMapper.writeValueAsMap(o);
  }

  @Override
  public Configuration read() {
    return new ConfigurationImpl(Collections.unmodifiableMap(map));
  }
}
