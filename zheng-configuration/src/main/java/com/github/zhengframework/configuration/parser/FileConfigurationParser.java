package com.github.zhengframework.configuration.parser;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;

public interface FileConfigurationParser {

  String[] supportFileTypes();

  Map<String, String> parse(String fileName, InputStream inputStream);

  default void checkSupportFileTypes(String fileName) {
    String s = Objects.requireNonNull(fileName);
    String type = s.substring(s.lastIndexOf("."));
    if (!ArrayUtils.contains(supportFileTypes(), type)) {
      throw new IllegalStateException("not support file type: " + type);
    }
  }
}
