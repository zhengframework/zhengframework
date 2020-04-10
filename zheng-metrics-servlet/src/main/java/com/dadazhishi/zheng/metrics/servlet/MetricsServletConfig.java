package com.github.zhengframework.metrics.servlet;

import com.github.zhengframework.configuration.spi.ConfigurationDefine;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetricsServletConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.metrics.servlet";
  private boolean enable = true;
  private String path = "/metrics";
}
