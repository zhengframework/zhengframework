package com.github.zhengframework.memcached;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.zhengframework.configuration.spi.ConfigurationDefine;
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
