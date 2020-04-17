package com.github.zhengframework.jdbc;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.dataSource", supportGroup = true)
public class DataSourceConfig implements ConfigurationDefine {
  private String catalog;
  private long connectionTimeout = TimeUnit.SECONDS.toMillis(30L);
  private long validationTimeout = TimeUnit.SECONDS.toMillis(5L);
  private long idleTimeout = TimeUnit.MINUTES.toMillis(10L);
  private long leakDetectionThreshold;
  private long maxLifetime = TimeUnit.MINUTES.toMillis(30L);
  private int maxPoolSize = -1;
  private int minIdle = -1;
  private String username;
  private String password;
  private long initializationFailTimeout = 1L;
  private String connectionInitSql;
  private String connectionTestQuery;
  private String dataSourceClassName;
  private String dataSourceJndiName;
  private String driverClassName;
  private String jdbcUrl;
  private String poolName;
  private String schema;
  private String transactionIsolationName;
  private boolean isAutoCommit = true;
  private boolean isReadOnly = false;
  private Map<String, String> properties = new HashMap<>();

}
