package com.github.zhengframework.jdbc.migrate;

/*-
 * #%L
 * zheng-migrate-liquibase
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

import com.github.zhengframework.jdbc.ManagedSchema;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LiquibaseManagedSchema implements ManagedSchema {

  private final LiquibaseConfig liquibaseConfig;

  public LiquibaseManagedSchema(LiquibaseConfig liquibaseConfig) {
    this.liquibaseConfig = liquibaseConfig;
  }

  @Override
  public void migrate(DataSource dataSource) {
    log.debug("liquibaseConfig={}", liquibaseConfig);
    if (liquibaseConfig.isEnable()) {
      log.info("Starting DB migration");
      try {
        Database database =
            DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));

        ClassLoaderResourceAccessor resourceAccessor =
            new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader());

        Liquibase liquibase =
            new Liquibase(liquibaseConfig.getChangeLogFile(), resourceAccessor, database);
        liquibase.update(new Contexts());
      } catch (Exception e) {
        log.error("DB migration fail", e);
      }
      log.info("DB migration success");
    }
  }
}
