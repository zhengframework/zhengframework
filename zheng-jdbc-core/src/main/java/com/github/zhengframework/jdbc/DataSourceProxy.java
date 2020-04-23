package com.github.zhengframework.jdbc;

import java.util.function.Function;
import javax.sql.DataSource;

public interface DataSourceProxy extends Function<DataSource, DataSource> {

  default int priority() {
    return 0;
  }

}
