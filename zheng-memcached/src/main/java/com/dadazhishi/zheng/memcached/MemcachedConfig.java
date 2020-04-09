package com.dadazhishi.zheng.memcached;

import com.dadazhishi.zheng.configuration.spi.ConfigurationDefine;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class MemcachedConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.memcached";
  private boolean group = false;
  private String addresses;
  @Nullable
  private String username;
  @Nullable
  private String password;

  @Nullable
  private AuthType authType;
}
