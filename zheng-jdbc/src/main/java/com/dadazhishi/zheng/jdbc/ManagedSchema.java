package com.dadazhishi.zheng.jdbc;

import javax.sql.DataSource;

public interface ManagedSchema
{
  DataSource migrate();
}
