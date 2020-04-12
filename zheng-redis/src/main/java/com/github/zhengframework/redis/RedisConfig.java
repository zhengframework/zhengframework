package com.github.zhengframework.redis;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.redis")
public class RedisConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.redis";
  private boolean group = false;
  private String host = "localhost";
  private String clientName;
  private int port = 6379;
  private int database = 0;
  private String password = null;
  private Boolean verifyPeer;
  private Boolean startTls;
  private Boolean ssl;
  private Long timeout = 6L;// seconds
}
