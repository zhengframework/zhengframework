package com.dadazhishi.zheng.memcached;

import javax.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemcachedConfig {

  public static final String NAMESPACE = "zheng.memcached";

  private String addresses;
  @Nullable
  private String username;
  @Nullable
  private String password;

  @Nullable
  private AuthType authType;
}
