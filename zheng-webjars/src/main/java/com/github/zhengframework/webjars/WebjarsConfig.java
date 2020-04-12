package com.github.zhengframework.webjars;

import com.github.zhengframework.configuration.ConfigurationDefine;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebjarsConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.webjars";
  private String basePath = "/webjars";
  private boolean disableCache = false;

}
