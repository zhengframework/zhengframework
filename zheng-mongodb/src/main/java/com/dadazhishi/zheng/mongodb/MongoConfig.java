package com.dadazhishi.zheng.mongodb;

import com.dadazhishi.zheng.configuration.spi.ConfigurationDefine;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MongoConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.mongo";
  private boolean group = false;
  private String url;
}
