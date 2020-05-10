package com.github.zhengframework.job;

/*-
 * #%L
 * zheng-job
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@ToString
@EqualsAndHashCode
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.job")
public class JobConfig implements ConfigurationDefine {

  private boolean enable = true;

  private String defaultTimezone;

  private Map<String, String> properties =
      new HashMap<String, String>() {
        private static final long serialVersionUID = 8840901850271582164L;

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
