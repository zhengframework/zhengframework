package com.dadazhishi.zheng.webjars;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebjarsConfig {

  public static final String PREFIX = "zheng.webjars";
  private String basePath = "/webjars";
  private boolean disableCache = false;

}
