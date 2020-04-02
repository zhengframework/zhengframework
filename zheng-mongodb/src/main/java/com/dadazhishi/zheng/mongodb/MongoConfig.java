package com.dadazhishi.zheng.mongodb;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MongoConfig {

  public static final String NAMESPACE = "zheng.mongo";

  private String uri;
}
