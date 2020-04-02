package com.dadazhishi.zheng.memcached;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemcachedConfig {

  public static final String NAMESPACE = "zheng.memcached";
  private String addresses;

  private String username;

  private String password;

  private AuthType authType;
}
