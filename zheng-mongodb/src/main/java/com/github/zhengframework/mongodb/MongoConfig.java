package com.github.zhengframework.mongodb;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.mongo")
public class MongoConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.mongo";
  private boolean group = false;
  private String url;
}
