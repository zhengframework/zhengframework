package com.github.zhengframework.web;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
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

  private String webSocketPath = "/ws";

  private int port = 8080;

  private boolean http2 = false;

  private boolean ssl = false;

  private int sslPort = 8443;

  private String keyStoreType;

  private String keyStorePath;

  private String keyStorePassword;

  private String trustStoreType;

  private String trustStorePath;

  private String trustStorePassword;

  private Map<String, String> properties = new HashMap<>();

  public void addProperty(String key, String value) {
    properties.put(key, value);
  }
}