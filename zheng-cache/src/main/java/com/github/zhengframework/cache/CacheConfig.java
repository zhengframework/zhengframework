package com.github.zhengframework.cache;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.cache")
public class CacheConfig implements ConfigurationDefine {

  private String cachingProviderName = null;
  private String cacheManagerResource = null;
  private String type = "simple";

}
