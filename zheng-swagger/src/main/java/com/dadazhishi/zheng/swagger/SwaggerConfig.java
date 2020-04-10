package com.github.zhengframework.swagger;

import com.github.zhengframework.configuration.spi.ConfigurationDefine;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class SwaggerConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.swagger";
  private String basePath = "/api-docs";

  private boolean enableUI = true;

  private boolean disableCache = false;

}