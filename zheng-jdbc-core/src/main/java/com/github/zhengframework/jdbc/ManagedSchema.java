package com.github.zhengframework.jdbc;

import javax.sql.DataSource;

public interface ManagedSchema {

  void migrate(DataSource dataSource);
}
