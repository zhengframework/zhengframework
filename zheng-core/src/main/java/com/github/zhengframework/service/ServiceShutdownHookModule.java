package com.github.zhengframework.service;

/*-
 * #%L
 * zheng-core
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

import com.google.inject.AbstractModule;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceShutdownHookModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SystemShutdownHook.class).asEagerSingleton();
  }

  @Singleton
  public static class SystemShutdownHook extends Thread {

    @Inject
    public SystemShutdownHook(final ServiceManager serviceManager) {
      Runtime.getRuntime()
          .addShutdownHook(
              new Thread(
                  () -> {
                    try {
                      log.info("Runtime Lifecycle shutdown hook begin running");
                      serviceManager.stop();
                    } catch (Throwable t) {
                      log.info("Runtime Lifecycle shutdown hook result in error ", t);
                      throw t;
                    }
                    log.info("Runtime Lifecycle shutdown hook finished running");
                  }));
    }
  }
}
