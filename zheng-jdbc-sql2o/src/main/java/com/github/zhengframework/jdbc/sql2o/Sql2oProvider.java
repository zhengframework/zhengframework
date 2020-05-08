package com.github.zhengframework.jdbc.sql2o;

/*-
 * #%L
 * zheng-jdbc-sql2o
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

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.sql2o.Sql2o;

public class Sql2oProvider implements Provider<Sql2o> {

  private Provider<DataSource> dataSourceProvider;

  @Inject
  public Sql2oProvider(Provider<DataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public Sql2o get() {
    return new Sql2o(dataSourceProvider.get());
  }
}
