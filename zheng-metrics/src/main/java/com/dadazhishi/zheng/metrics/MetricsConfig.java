package com.github.zhengframework.metrics;

import com.github.zhengframework.configuration.spi.ConfigurationDefine;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetricsConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.metrics";

  private boolean enable = true;
}
