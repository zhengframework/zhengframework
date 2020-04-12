package com.github.zhengframework.rest;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.rest")
public class RestConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.rest";
  private String path = null;

  private Map<String, String> properties = new HashMap<>();

  public void addProperty(String key, String value) {
    properties.put(key, value);
  }
}
