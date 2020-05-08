package com.github.zhengframework.jdbc.wrapper;

/*-
 * #%L
 * zheng-jdbc
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

import com.github.zhengframework.jdbc.DataSourceConfig;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.concurrent.locks.ReentrantLock;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class DataSourceWrapperProvider implements Provider<DataSourceWrapper> {

  private final Provider<DataSourceConfig> dataSourceConfigProvider;
  private final Provider<Injector> injectorProvider;
  private DataSourceWrapper dataSourceWrapper;
  private ReentrantLock lock = new ReentrantLock();

  @Inject
  public DataSourceWrapperProvider(
      Provider<DataSourceConfig> dataSourceConfigProvider, Provider<Injector> injectorProvider) {
    this.dataSourceConfigProvider = dataSourceConfigProvider;
    this.injectorProvider = injectorProvider;
  }

  @Override
  public DataSourceWrapper get() {
    lock.lock();
    try {
      if (dataSourceWrapper == null) {
        DataSourceConfig dataSourceConfig = dataSourceConfigProvider.get();
        Injector injector = injectorProvider.get();
        DataSourceWrapperFactory factory = new DataSourceWrapperFactory();
        try {
          dataSourceWrapper = factory.create(dataSourceConfig, injector);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    } finally {
      lock.unlock();
    }
    return dataSourceWrapper;
  }
}
