package com.dadazhishi.zheng.rest;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class RestConfig {

  public static final String PREFIX = "zheng.rest";
  private String path = null;

  private Map<String, String> properties = new HashMap<>();

  public void addProperty(String key, String value) {
    properties.put(key, value);
  }
}
