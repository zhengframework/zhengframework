package com.dadazhishi.zheng.configuration.parser;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;

public class FileConfigurationParserSelector implements FileConfigurationParser {

  private final List<FileConfigurationParser> parsers;
  private final String[] supportFileTypes;

  public FileConfigurationParserSelector(
      List<FileConfigurationParser> parsers) {
    this.parsers = Objects.requireNonNull(parsers);

    supportFileTypes = parsers.stream().map(FileConfigurationParser::supportFileTypes)
        .flatMap(Arrays::stream)
        .distinct().toArray(String[]::new);
  }

  @Override
  public String[] supportFileTypes() {
    return supportFileTypes;
  }

  @Override
  public Map<String, String> parse(String fileName, InputStream inputStream) {
    String s = Objects.requireNonNull(fileName);
    String type = s.substring(s.lastIndexOf("."));
    Optional<FileConfigurationParser> first = parsers.stream()
        .filter(p -> ArrayUtils.contains(p.supportFileTypes(), type))
        .findFirst();
    if (first.isPresent()) {
      return first.get().parse(fileName, inputStream);
    }
    throw new IllegalStateException("not find any FileConfigurationParser for type: " + type);
  }
}
