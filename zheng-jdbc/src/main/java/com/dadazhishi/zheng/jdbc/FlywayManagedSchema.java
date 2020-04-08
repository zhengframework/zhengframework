package com.dadazhishi.zheng.jdbc;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

@Slf4j
public class FlywayManagedSchema implements ManagedSchema {

  private final DataSource dataSource;

  public FlywayManagedSchema(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  @Override
  public DataSource migrate() {
    log.info("Starting DB migration");

    val flyway = Flyway.configure().dataSource(dataSource)
        .load();

    MigrationInfo current = flyway.info().current();
    if (current == null)
    {
      log.info("No existing schema found");
    }
    else
    {
      log.info("Current schema version is {}", current.getVersion());
    }

    flyway.migrate();

    log.info("Schema migrated to version {}", flyway.info().current().getVersion());

    return dataSource;
  }
}
