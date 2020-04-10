package com.github.zhengframework.jdbc;

import java.sql.SQLException;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LiquibaseManagedSchema implements ManagedSchema {

  private final DataSource dataSource;

  public LiquibaseManagedSchema(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public DataSource migrate() {
    log.info("Starting DB migration");
    try {
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
      String changeLogFile = "";
      ClassLoaderResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(
          Thread.currentThread().getContextClassLoader());

      Liquibase liquibase = new Liquibase(changeLogFile, resourceAccessor, database);
      liquibase.update(new Contexts());
    } catch (SQLException | LiquibaseException e) {
      log.error("DB migration fail", e);
    }
    log.info("DB migration success");
    return dataSource;
  }
}
