package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileConfigurationResolver extends AbstractConfigurationResolver {

  private Map<String, String> map = new HashMap<>();

  public FileConfigurationResolver(
      ConfigurationParser<InputStream> parser, File file) {
    try (FileInputStream inputStream = new FileInputStream(file)) {
      map.putAll(parser.parse(inputStream));
    } catch (Exception e) {
      throw new RuntimeException(
          "read configuration from file fail, file=" + file.getAbsolutePath(), e);
    }
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }
}
