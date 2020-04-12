package com.github.zhengframework.swagger;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.swagger")
public final class SwaggerConfig implements ConfigurationDefine {

  private String docsPath = "/api-docs";

  private String apiUrl = "http://127.0.0.1:8080/openapi.json";

  private boolean enableUI = true;

  private boolean disableCache = false;

}