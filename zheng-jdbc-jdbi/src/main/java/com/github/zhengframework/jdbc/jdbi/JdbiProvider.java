package com.github.zhengframework.jdbc.jdbi;

/*-
 * #%L
 * zheng-jdbc-jdbi
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

import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;

public class JdbiProvider implements Provider<Jdbi> {

  private final Provider<DataSource> dataSourceProvider;
  private final Provider<List<JdbiPlugin>> jdbiPluginListProvider;

  @Inject
  public JdbiProvider(Provider<DataSource> dataSourceProvider,
      Provider<List<JdbiPlugin>> jdbiPluginListProvider) {
    this.dataSourceProvider = dataSourceProvider;
    this.jdbiPluginListProvider = jdbiPluginListProvider;
  }

  @Override
  public Jdbi get() {
    List<JdbiPlugin> jdbiPluginList = jdbiPluginListProvider.get();
    Jdbi jdbi = Jdbi.create(dataSourceProvider.get());
    for (JdbiPlugin jdbiPlugin : jdbiPluginList) {
      jdbi.installPlugin(jdbiPlugin);
    }
    return jdbi;
  }
}
