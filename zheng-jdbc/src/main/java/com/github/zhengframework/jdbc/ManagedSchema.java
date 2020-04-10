package com.github.zhengframework.jdbc;

import javax.sql.DataSource;

public interface ManagedSchema {

  DataSource migrate();
}
