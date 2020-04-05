package com.dadazhishi.zheng.swagger;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class SwaggerConfig {

  public static final String PREFIX = "zheng.swagger";
  private String basePath = "/api-docs";

  private boolean enableUI = true;

  private boolean disableCache = false;

}