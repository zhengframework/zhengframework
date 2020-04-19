package com.github.zhengframework.healthcheck;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@ConfigurationInfo(prefix = "zheng.healthCheck")
public class HealthCheckConfig implements ConfigurationDefine {

  private boolean enable = true;
  private long duration = 60;
  private TimeUnit unit = TimeUnit.SECONDS;

}
