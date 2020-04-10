package com.github.zhengframework.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.zhengframework.configuration.spi.ConfigurationDefine;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class MongoConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.mongo";
  private boolean group = false;
  private String url;
}
