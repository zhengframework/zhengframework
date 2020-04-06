package com.dadazhishi.zheng.redis;

import com.dadazhishi.zheng.configuration.spi.ConfigurationDefine;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
