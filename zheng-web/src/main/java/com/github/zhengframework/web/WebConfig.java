package com.github.zhengframework.web;

import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import com.github.zhengframework.configuration.spi.ConfigurationDefine;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.web")
public class WebConfig implements ConfigurationDefine {

  private String contextPath = "/";

  private int port = 8080;

  private Map<String, String> properties = new HashMap<>();

  public void addProperty(String key, String value) {
    properties.put(key, value);
  }
}