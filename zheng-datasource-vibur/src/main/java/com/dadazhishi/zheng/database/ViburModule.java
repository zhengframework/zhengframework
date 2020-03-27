package com.dadazhishi.zheng.database;

import com.google.inject.Provides;
import java.util.Properties;
import javax.sql.DataSource;
import org.vibur.dbcp.ViburDBCPDataSource;

public class ViburModule extends AbstractDataBaseModule {

  public ViburModule() {
    this(null);
  }

  public ViburModule(DataBaseConfig config) {
    super(config);
  }

  @Provides
  public DataSource dataSource(final DataBaseConfig config) {
    ViburDBCPDataSource ds = new ViburDBCPDataSource();
    ds.setDriverClassName(config.getDriverClassName());
    ds.setJdbcUrl(config.getUrl());
    ds.setUsername(config.getUsername());
    ds.setPassword(config.getPassword());
    Properties properties = new Properties();
    properties.putAll(config.getProperties());
    ds.setDriverProperties(properties);
    return ds;
  }
}
