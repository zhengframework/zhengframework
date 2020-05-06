package com.github.zhengframework.jdbc.migrate;

/*-
 * #%L
 * zheng-migrate-flyway
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
import javax.inject.Inject;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

@Slf4j
public class FlywayManagedSchema implements ManagedSchema {

  private final FlywayConfig flywayConfig;

  @Inject
  public FlywayManagedSchema(FlywayConfig flywayConfig) {
    this.flywayConfig = flywayConfig;
  }

  @Override
  public void migrate(DataSource dataSource) {
    log.debug("flywayConfig={}", flywayConfig);
    if (flywayConfig.isEnable()) {
      try {
        log.info("Starting DB migration");
        Flyway flyway =
            Flyway.configure().dataSource(dataSource).locations(flywayConfig.getLocation()).load();

        MigrationInfo current = flyway.info().current();
        if (current == null) {
          log.info("No existing schema found");
        } else {
          log.info("Current schema version is {}", current.getVersion());
        }

        flyway.migrate();
        log.info("Schema migrated to version {}", flyway.info().current().getVersion());
      } catch (Exception e) {
        log.error("DB migration fail", e);
        throw new RuntimeException(e);
      }
      log.info("DB migration success");
    }
  }
}
