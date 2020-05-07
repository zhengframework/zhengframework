package com.github.zhengframework.healthcheck.sys;

/*-
 * #%L
 * zheng-healthcheck
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

import com.github.zhengframework.healthcheck.NamedHealthCheck;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class SystemLoadHealthCheck extends NamedHealthCheck {

  @Override
  public String getName() {
    return "SystemLoadHealthCheck";
  }

  @Override
  protected Result check() throws Exception {
    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    double load = operatingSystemMXBean.getSystemLoadAverage();
    int cpu = operatingSystemMXBean.getAvailableProcessors();
    if (load < cpu) {
      return Result.healthy();
    } else {
      return Result.unhealthy("load:%s,cpu:%s", load, cpu);
    }
  }
}
