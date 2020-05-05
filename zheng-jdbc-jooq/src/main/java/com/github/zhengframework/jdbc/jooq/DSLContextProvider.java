package com.github.zhengframework.jdbc.jooq;

/*-
 * #%L
 * zheng-jdbc-jooq
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
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

public class DSLContextProvider implements Provider<DSLContext> {

  private Provider<DataSource> dataSourceProvider;
  private Provider<SQLDialect> sqlDialectProvider;
  private Provider<Settings> settingsProvider;

  @Inject
  public DSLContextProvider(Provider<DataSource> dataSourceProvider,
      Provider<SQLDialect> sqlDialectProvider, Provider<Settings> settingsProvider) {
    this.dataSourceProvider = dataSourceProvider;
    this.sqlDialectProvider = sqlDialectProvider;
    this.settingsProvider = settingsProvider;
  }

  @Override
  public DSLContext get() {
    return DSL.using(dataSourceProvider.get(), sqlDialectProvider.get(), settingsProvider.get());
  }
}
