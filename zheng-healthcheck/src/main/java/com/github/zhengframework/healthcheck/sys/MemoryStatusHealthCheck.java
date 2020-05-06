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

public class MemoryStatusHealthCheck extends NamedHealthCheck {

  private final long availableMemThreshold;

  public MemoryStatusHealthCheck(long availableMemThreshold) {
    this.availableMemThreshold = availableMemThreshold;
  }

  public MemoryStatusHealthCheck() {
    this(50 * 1024 * 1024);
  }

  @Override
  public String getName() {
    return "MemoryStatusHealthCheck";
  }

  @Override
  protected Result check() {
    Runtime runtime = Runtime.getRuntime();
    long freeMemory = runtime.freeMemory();
    long totalMemory = runtime.totalMemory();
    long maxMemory = runtime.maxMemory();
    boolean ok = maxMemory - (totalMemory - freeMemory) > availableMemThreshold;
    if (ok) {
      return Result.healthy();
    } else {
      String msg =
          "max:"
              + (maxMemory / 1024 / 1024)
              + "M,total:"
              + (totalMemory / 1024 / 1024)
              + "M,used:"
              + ((totalMemory / 1024 / 1024) - (freeMemory / 1024 / 1024))
              + "M,free:"
              + (freeMemory / 1024 / 1024)
              + "M";
      return Result.unhealthy(msg);
    }
  }
}
