package com.dadazhishi.zheng.metrics;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetricsConfig {

  public static final String PREFIX = "zheng.metrics";

  private boolean enable = true;
}
