package com.github.zhengframework.jdbc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import java.sql.SQLException;
import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConnectionProvider;

@Slf4j
@Singleton
public class JooqPersistService implements Provider<DSLContext>, UnitOfWork, PersistService {


  private final ThreadLocal<DSLContext> threadFactory = new ThreadLocal<DSLContext>();
  private final ThreadLocal<DefaultConnectionProvider> threadConnection = new ThreadLocal<DefaultConnectionProvider>();
  private final DataSource jdbcSource;
  private final SQLDialect sqlDialect;

  @Inject(optional = true)
  private Settings jooqSettings = null;

  @Inject(optional = true)
  private Configuration configuration = null;

  @Inject
  public JooqPersistService(final DataSource jdbcSource, final SQLDialect sqlDialect) {
    this.jdbcSource = jdbcSource;
    this.sqlDialect = sqlDialect;
  }

  @Override
  public DSLContext get() {
    DSLContext factory = threadFactory.get();
    if (null == factory) {
      throw new IllegalStateException("Requested Factory outside work unit. "
          + "Try calling UnitOfWork.begin() first, use @Transactional annotation"
          + "or use a PersistFilter if you are inside a servlet environment.");
    }
    return factory;
  }

  public boolean isWorking() {
    return threadFactory.get() != null;
  }

  public DefaultConnectionProvider getConnectionWrapper() {
    return threadConnection.get();
  }

  @Override
  public void start() {
    if (null != threadFactory.get()) {
      throw new IllegalStateException("Work already begun on this thread. "
          + "It looks like you have called UnitOfWork.begin() twice"
          + " without a balancing call to end() in between.");
    }
    DefaultConnectionProvider conn;
    try {
      log.debug("Getting JDBC connection");
      conn = new DefaultConnectionProvider(jdbcSource.getConnection());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    DSLContext jooqFactory;

    if (configuration != null) {
      log.debug("Creating factory from configuration having dialect {}", configuration.dialect());
      if (jooqSettings != null) {
        log.warn(
            "@Injected org.jooq.conf.Settings is being ignored since a full org.jooq.Configuration was supplied");
      }
      jooqFactory = DSL.using(configuration);
    } else {
      if (jooqSettings == null) {
        log.debug("Creating factory with dialect {}", sqlDialect);
        jooqFactory = DSL.using(conn, sqlDialect);
      } else {
        log.debug("Creating factory with dialect {} and settings.", sqlDialect);
        jooqFactory = DSL.using(conn, sqlDialect, jooqSettings);
      }
    }
    threadConnection.set(conn);
    threadFactory.set(jooqFactory);


  }

  @Override
  public void stop() {

  }

  @Override
  public void begin() {

  }

  @Override
  public void end() {
    DSLContext jooqFactory = threadFactory.get();
    DefaultConnectionProvider conn = threadConnection.get();
    // Let's not penalize users for calling end() multiple times.
    if (null == jooqFactory) {
      return;
    }

    try {
      log.debug("Closing JDBC connection");
      conn.acquire().close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    threadFactory.remove();
    threadConnection.remove();
  }
}
