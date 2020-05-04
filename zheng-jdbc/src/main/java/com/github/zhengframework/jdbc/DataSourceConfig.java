package com.github.zhengframework.jdbc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import com.github.zhengframework.jdbc.wrapper.DataSourceWrapper;
import com.github.zhengframework.jdbc.wrapper.hikari.HikariDataSourceWrapper;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.dataSource", supportGroup = true)
public class DataSourceConfig implements ConfigurationDefine {

  private String username;
  private String password;
  private String driverClassName;
  private String url;
  private Integer loginTimeout;
  private Class<? extends DataSourceWrapper> dataSourceWrapperClass = HikariDataSourceWrapper.class;
  @JsonIgnore
  private Map<String, String> properties = new HashMap<>();

}
