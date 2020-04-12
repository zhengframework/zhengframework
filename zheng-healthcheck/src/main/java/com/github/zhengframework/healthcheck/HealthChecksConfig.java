package com.github.zhengframework.healthcheck;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import io.dropwizard.util.Duration;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.healthCheck")
public class HealthChecksConfig implements ConfigurationDefine {

  private String metricsPrefix = "zheng.healthCheck";
  private Duration interval = Duration.minutes(10);

}
