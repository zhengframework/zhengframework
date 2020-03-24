package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class RefreshableFileConfigurationResolver extends RefreshableConfigurationResolver {

  private final ConfigurationParser<InputStream> parser;

  private final File file;

  public RefreshableFileConfigurationResolver(
      ConfigurationParser<InputStream> parser, File file) {
    this.parser = parser;
    this.file = file;
  }

  @Override
  public void doUpdate() {
    try (FileInputStream inputStream = new FileInputStream(file)) {
      Map<String, String> map = parser.parse(inputStream);
      update(map);
    } catch (Exception e) {
      throw new RuntimeException(
          "read configuration from file fail, file=" + file.getAbsolutePath(), e);
    }
  }
}
