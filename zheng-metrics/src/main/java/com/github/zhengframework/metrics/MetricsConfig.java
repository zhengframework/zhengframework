package com.github.zhengframework.metrics;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.metrics")
public class MetricsConfig implements ConfigurationDefine {

  private boolean enable = true;
}
