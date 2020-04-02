package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.spi.AutoConfigurationParser;
import com.google.common.base.Joiner;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

public class AutoConfigurationParserSelector {

  private Map<String, AutoConfigurationParser> parserMap;

  public AutoConfigurationParserSelector(
      Map<String, String> properties) {
    ServiceLoader<AutoConfigurationParser> autoConfigurationParsers = ServiceLoader
        .load(AutoConfigurationParser.class);
    parserMap = new HashMap<>();

    for (AutoConfigurationParser next : autoConfigurationParsers) {
      next.init(properties);
      for (String fileType : next.fileTypes()) {
        parserMap.put(fileType.toLowerCase(), next);
      }
    }
  }

  public Map<String, String> parse(URI uri, InputStream content) {
    String path = uri.getSchemeSpecificPart();
    Objects.requireNonNull(path);
    String fileType = path.substring(path.lastIndexOf("."));
    fileType = fileType.toLowerCase();
    AutoConfigurationParser parser = parserMap.get(fileType);
    if (parser == null) {
      throw new RuntimeException(
          "invalid file type, support type: " + Joiner.on(",").join(parserMap.keySet()));
    }
    return parser.parse(content);
  }


}
