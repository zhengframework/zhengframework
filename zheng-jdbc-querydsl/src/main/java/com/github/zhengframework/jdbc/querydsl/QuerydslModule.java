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

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.EqualsAndHashCode;

/**
 * http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s03.html
 */
@EqualsAndHashCode(callSuper = false)
public class QuerydslModule extends ConfigurationAwareModule {


  @Override
  protected void configure() {
    Map<String, QuerydslConfig> configMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), QuerydslConfig.class);
    for (Entry<String, QuerydslConfig> entry : configMap.entrySet()) {
      String name = entry.getKey();
      QuerydslConfig querydslConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(Key.get(QuerydslConfig.class)).toInstance(querydslConfig);
        if (querydslConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(
              com.querydsl.sql.Configuration.class)).setDefault()
              .toProvider(() -> new com.querydsl.sql.Configuration(
                  SQLTemplates.DEFAULT));
          bind(Key.get(SQLQueryFactory.class)).toProvider(
              new SQLQueryFactoryProvider(getProvider(Key.get(DataSource.class)),
                  getProvider(Key.get(
                      com.querydsl.sql.Configuration.class)))).in(Singleton.class);
        }
      } else {
        bind(Key.get(QuerydslConfig.class, named(name))).toInstance(querydslConfig);
        if (querydslConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(
              com.querydsl.sql.Configuration.class, named(name))).setDefault()
              .toProvider(() -> new com.querydsl.sql.Configuration(
                  SQLTemplates.DEFAULT));
          bind(Key.get(SQLQueryFactory.class, named(name))).toProvider(
              new SQLQueryFactoryProvider(getProvider(Key.get(DataSource.class, named(name))),
                  getProvider(Key.get(
                      com.querydsl.sql.Configuration.class, named(name))))).in(Singleton.class);
        }
      }
    }
  }

}
