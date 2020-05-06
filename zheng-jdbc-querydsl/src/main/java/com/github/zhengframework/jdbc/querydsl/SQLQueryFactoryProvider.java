package com.github.zhengframework.jdbc.querydsl;

/*-
 * #%L
 * zheng-jdbc-querydsl
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

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQueryFactory;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

public class SQLQueryFactoryProvider implements Provider<SQLQueryFactory> {

  private Provider<DataSource> dataSourceProvider;
  private Provider<Configuration> configurationProvider;

  @Inject
  public SQLQueryFactoryProvider(
      Provider<DataSource> dataSourceProvider, Provider<Configuration> configurationProvider) {
    this.dataSourceProvider = dataSourceProvider;
    this.configurationProvider = configurationProvider;
  }

  @Override
  public SQLQueryFactory get() {
    return new SQLQueryFactory(configurationProvider.get(), dataSourceProvider.get());
  }
}
