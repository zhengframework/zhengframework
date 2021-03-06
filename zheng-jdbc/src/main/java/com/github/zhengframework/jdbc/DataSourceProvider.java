package com.github.zhengframework.jdbc;

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

import com.github.zhengframework.jdbc.wrapper.DataSourceWrapper;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class DataSourceProvider implements Provider<DataSource> {

  private final Provider<Set<DataSourceProxy>> dataSourceProxySetProvider;
  private final Provider<ManagedSchema> managedSchemaProvider;
  private Provider<DataSourceWrapper> dataSourceWrapperProvider;
  private DataSource copyDataSource;
  private ReentrantLock lock = new ReentrantLock();

  @Inject
  public DataSourceProvider(
      Provider<DataSourceWrapper> dataSourceWrapperProvider,
      Provider<Set<DataSourceProxy>> dataSourceProxySetProvider,
      Provider<ManagedSchema> managedSchemaProvider) {
    this.dataSourceWrapperProvider = dataSourceWrapperProvider;
    this.dataSourceProxySetProvider = dataSourceProxySetProvider;
    this.managedSchemaProvider = managedSchemaProvider;
  }

  @Override
  public DataSource get() {
    lock.lock();
    try {
      if (copyDataSource == null) {
        List<DataSourceProxy> collect =
            dataSourceProxySetProvider.get().stream()
                .sorted(Comparator.comparing(DataSourceProxy::priority))
                .collect(Collectors.toList());
        copyDataSource = dataSourceWrapperProvider.get();
        for (DataSourceProxy dataSourceProxy : collect) {
          copyDataSource = dataSourceProxy.apply(copyDataSource);
        }
        managedSchemaProvider.get().migrate(copyDataSource);
      }
    } finally {
      lock.unlock();
    }
    return copyDataSource;
  }
}
