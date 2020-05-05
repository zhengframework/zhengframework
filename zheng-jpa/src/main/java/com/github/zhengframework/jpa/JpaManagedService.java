package com.github.zhengframework.jpa;

/*-
 * #%L
 * zheng-jpa
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

import com.github.zhengframework.service.Service;
import javax.inject.Inject;

public class JpaManagedService implements Service {

  private final JpaService jpaService;

  @Inject
  public JpaManagedService(
      JpaService jpaService) {
    this.jpaService = jpaService;
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  public void start() throws Exception {
    jpaService.start();
  }

  @Override
  public void stop() throws Exception {
    jpaService.stop();
  }
}
