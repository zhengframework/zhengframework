package com.github.zhengframework.metrics.servlet;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.metrics.servlet")
public class MetricsServletConfig implements ConfigurationDefine {

  private boolean enable = true;
  private String path = "/metrics";
}
