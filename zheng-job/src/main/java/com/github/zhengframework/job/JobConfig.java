package com.github.zhengframework.job;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.job")
public class JobConfig implements ConfigurationDefine {

  private boolean enable = true;

  private String defaultTimezone;

  private Map<String, String> properties = new HashMap<String, String>() {
    {
      put("org.quartz.scheduler.instanceName", "scheduler");
      put("org.quartz.scheduler.instanceId", "AUTO");
      put("org.quartz.scheduler.skipUpdateCheck", "true");
      put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
      put("org.quartz.threadPool.threadCount", "10");
      put("org.quartz.threadPool.threadPriority", "5");
      put("org.quartz.jobStore.misfireThreshold", "60000");
    }
  };

}
