package com.dadazhishi.zheng.configuration.parser;

import java.io.InputStream;
import java.util.Map;

public interface ConfigurationParser {

  Map<String, String> parse(InputStream inputStream);
}
