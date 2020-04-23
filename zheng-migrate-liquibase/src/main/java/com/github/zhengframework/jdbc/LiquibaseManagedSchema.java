package com.github.zhengframework.jdbc;

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
        Database database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));

        ClassLoaderResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(
            Thread.currentThread().getContextClassLoader());

        Liquibase liquibase = new Liquibase(liquibaseConfig.getChangeLogFile(), resourceAccessor,
            database);
        liquibase.update(new Contexts());
      } catch (Exception e) {
        log.error("DB migration fail", e);
      }
      log.info("DB migration success");
    }
  }
}
