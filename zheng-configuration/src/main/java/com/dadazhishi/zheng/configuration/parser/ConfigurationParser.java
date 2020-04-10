package com.github.zhengframework.configuration.parser;

import java.io.InputStream;
import java.util.Map;

public interface ConfigurationParser {

  Map<String, String> parse(InputStream inputStream);
}
