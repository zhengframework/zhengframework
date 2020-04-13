package com.github.zhengframework.memcached;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.memcached", supportGroup = true)
public class MemcachedConfig implements ConfigurationDefine {

  private String addresses = "localhost:11211";

  private String username;

  private String password;

  private AuthType authType;
}
