package com.dadazhishi.zheng.metrics.servlet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetricsServletConfig {

  public static final String NAMESPACE = "zheng.metrics.servlet";
  private String path = "/metrics";
}
