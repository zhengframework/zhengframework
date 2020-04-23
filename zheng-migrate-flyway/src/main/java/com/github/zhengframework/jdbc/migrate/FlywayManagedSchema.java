package com.github.zhengframework.jdbc.migrate;

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
        Flyway flyway = Flyway.configure().dataSource(dataSource)
            .locations(flywayConfig.getLocation())
            .load();

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
