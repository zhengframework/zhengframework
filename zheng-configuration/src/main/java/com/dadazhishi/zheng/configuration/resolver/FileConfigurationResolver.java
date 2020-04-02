package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileConfigurationResolver extends AbstractConfigurationResolver {

  private Map<String, String> map = new HashMap<>();

  public FileConfigurationResolver(
      ConfigurationParser<InputStream> parser, File file) {
    this(parser, file, true);
  }

  public FileConfigurationResolver(
      ConfigurationParser<InputStream> parser, File file, boolean failOnError) {
    try (FileInputStream inputStream = new FileInputStream(file)) {
      map.putAll(parser.parse(inputStream));
    } catch (Exception e) {
      if (failOnError) {
        throw new RuntimeException(
            "read configuration from file fail, file=" + file, e);
      } else {
        log.warn("read configuration from file fail, file={}", file);
      }
    }
  }

  @Override
  protected Map<String, String> delegate() {
    return map;
  }
}
