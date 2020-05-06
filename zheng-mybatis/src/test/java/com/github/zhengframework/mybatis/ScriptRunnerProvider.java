package com.github.zhengframework.mybatis;

/*-
 * #%L
 * zheng-mybatis
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
