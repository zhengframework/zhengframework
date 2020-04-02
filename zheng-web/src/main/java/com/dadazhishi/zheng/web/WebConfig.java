package com.dadazhishi.zheng.web;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebConfig {

  public static final String NAMESPACE = "zheng.web";
  private String contextPath = "/";

  private int port = 8080;

  private Map<String, String> properties = new HashMap<>();

  public void addProperty(String key, String value) {
    properties.put(key, value);
  }
}