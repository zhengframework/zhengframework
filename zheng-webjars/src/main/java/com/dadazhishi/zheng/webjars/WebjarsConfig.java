package com.dadazhishi.zheng.webjars;

import com.dadazhishi.zheng.configuration.spi.ConfigurationDefine;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebjarsConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.webjars";
  private String basePath = "/webjars";
  private boolean disableCache = false;

}
