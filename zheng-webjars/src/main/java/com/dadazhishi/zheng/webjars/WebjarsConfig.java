package com.dadazhishi.zheng.webjars;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebjarsConfig {

  public static final String PREFIX = "zheng.webjars";
  public static String WEBJAR_URL_PREFIX = "/webjars/";
  private String path = "/webjars";
  private boolean disableCache = false;

}
