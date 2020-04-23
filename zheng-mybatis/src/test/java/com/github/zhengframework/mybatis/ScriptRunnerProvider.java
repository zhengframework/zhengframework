package com.github.zhengframework.mybatis;

import java.sql.Connection;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.inject.Provider;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;

public class ScriptRunnerProvider implements Provider<ScriptRunner> {

  private final SqlSessionFactory sqlSessionFactory;

  @Inject
  public ScriptRunnerProvider(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Override
  public ScriptRunner get() {
    Environment environment = sqlSessionFactory.getConfiguration().getEnvironment();
    try {
      Connection connection = environment.getDataSource().getConnection();
      return new ScriptRunner(connection);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
